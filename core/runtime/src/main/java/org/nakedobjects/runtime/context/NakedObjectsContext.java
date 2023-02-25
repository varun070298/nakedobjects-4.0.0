package org.nakedobjects.runtime.context;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.TransactionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugList;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


/**
 * Provides singleton <i>access to</i> the current (session scoped) {@link NakedObjectSession}, along with
 * convenience methods to obtain application-scoped components and also any transaction-scoped components
 * {@link TransactionScopedComponent}s if a {@link NakedObjectTransaction}
 * {@link NakedObjectSession#getCurrentTransaction() is in progress}.
 * 
 * <p>
 * Somewhat analogous to (the static methods in) <tt>HibernateUtil</tt>.
 */
public abstract class NakedObjectsContext implements DebugInfo {

    private static final Logger LOG = Logger.getLogger(NakedObjectsContext.class);

    private static NakedObjectsContext singleton;

    private final NakedObjectSessionFactory sessionFactory;
    private final ContextReplacePolicy replacePolicy;
    private final SessionClosePolicy sessionClosePolicy;

    private static NakedObjectConfiguration configuration;

    // ///////////////////////////////////////////////////////////
    // Singleton & Constructor
    // ///////////////////////////////////////////////////////////

    /**
     * Returns the singleton providing access to the set of execution contexts.
     */
    public static NakedObjectsContext getInstance() {
        return singleton;
    }

    /**
     * Whether a singleton has been created using {@link #getInstance()}.
     */
    public static boolean exists() {
        return singleton != null;
    }

    /**
     * Resets the singleton, so another can created.
     * 
     * @see #NakedObjects()
     */
    public static void testReset() {
        singleton = null;
    }

    protected static enum SessionClosePolicy {
        /**
         * Sessions must be explicitly closed.
         */
        EXPLICIT_CLOSE,
        /**
         * Sessions will be automatically closed.
         */
        AUTO_CLOSE;
    }

    /**
     * Whether the {@link NakedObjectsContext#getInstance() singleton} itself may be replaced.
     */
    protected static enum ContextReplacePolicy {
        NOT_REPLACEABLE, REPLACEABLE
    }

    /**
     * Creates a new instance of the {@link NakedObjectSession} holder.
     * 
     * <p>
     * Will throw an exception if an instance has already been created and is not
     * {@link ContextReplacePolicy#REPLACEABLE}.
     */
    protected NakedObjectsContext(
            final ContextReplacePolicy replacePolicy,
            final SessionClosePolicy sessionClosePolicy,
            final NakedObjectSessionFactory sessionFactory) {
        if (singleton != null && !singleton.isContextReplaceable()) {
            throw new NakedObjectException("Naked Objects Context already set up and cannot be replaced");
        }
        singleton = this;
        this.sessionFactory = sessionFactory;
        this.sessionClosePolicy = sessionClosePolicy;
        this.replacePolicy = replacePolicy;
    }

    // ///////////////////////////////////////////////////////////
    // SessionFactory
    // ///////////////////////////////////////////////////////////

    /**
     * As injected in constructor.
     */
    public final NakedObjectSessionFactory getSessionFactoryInstance() {
        return sessionFactory;
    }

    // ///////////////////////////////////////////////////////////
    // Policies
    // ///////////////////////////////////////////////////////////

    /**
     * Whether a context singleton can simply be replaced or not.
     */
    public final boolean isContextReplaceable() {
        return replacePolicy == ContextReplacePolicy.REPLACEABLE;
    }

    /**
     * Whether any open session can be automatically {@link #closeSessionInstance() close}d on
     * {@link #openSessionInstance(AuthenticationSession) open}.
     */
    public final boolean isSessionAutocloseable() {
        return sessionClosePolicy == SessionClosePolicy.AUTO_CLOSE;
    }

    /**
     * Helper method for subclasses' implementation of {@link #openSessionInstance(AuthenticationSession)}.
     */
    protected void applySessionClosePolicy() {
        if (getSessionInstance() == null) {
            return;
        }
        if (!isSessionAutocloseable()) {
            throw new IllegalStateException("Session already open and context not configured for autoclose");
        }
        closeSessionInstance();
    }

    // ///////////////////////////////////////////////////////////
    // open / close / shutdown
    // ///////////////////////////////////////////////////////////

    /**
     * Creates a new {@link NakedObjectSession} and binds into the current context.
     * 
     * @throws IllegalStateException
     *             if already opened.
     */
    public abstract NakedObjectSession openSessionInstance(AuthenticationSession session);

    /**
     * Closes the {@link NakedObjectSession} for the current context.
     * 
     * <p>
     * Ignored if already closed.
     */
    public final void closeSessionInstance() {
        if (getSessionInstance() != null) {
            getSessionInstance().close();
            doClose();
        }
    }

    /**
     * Overridable hook method called from {@link #closeSessionInstance()}, allowing subclasses to clean up
     * (for example datastructures).
     * 
     * <p>
     * The {@link #getSessionInstance() current} {@link NakedObjectSession} will already have been
     * {@link NakedObjectSession#close() closed}.
     */
    protected void doClose() {}

    /**
     * Shutdown the application.
     */
    protected abstract void closeAllSessionsInstance();

    

    // ///////////////////////////////////////////////////////////
    // getSession()
    // ///////////////////////////////////////////////////////////

    /**
     * Locates the current {@link NakedObjectSession}.
     * 
     * <p>
     * This might just be a singleton (eg {@link NakedObjectsContextStatic}), or could be retrieved from the
     * thread (eg {@link NakedObjectsContextThreadLocal}).
     */
    public abstract NakedObjectSession getSessionInstance();

    /**
     * The {@link NakedObjectSession} for specified {@link NakedObjectSession#getId()}.
     */
    protected abstract NakedObjectSession getSessionInstance(String sessionId);

    /**
     * All known session Ids.
     * 
     * <p>
     * Provided primarily for debugging.
     */
    public abstract String[] allSessionIds();

    // ///////////////////////////////////////////////////////////
    // Static Convenience methods (session management)
    // ///////////////////////////////////////////////////////////

    /**
     * Convenience method to open a new {@link NakedObjectSession}.
     * 
     * @see #openSessionInstance(AuthenticationSession)
     */
    public static NakedObjectSession openSession(final AuthenticationSession authenticationSession) {
        return getInstance().openSessionInstance(authenticationSession);
    }

    /**
     * Convenience method to close the current {@link NakedObjectSession}.
     * 
     * @see #closeSessionInstance()
     */
    public static void closeSession() {
        getInstance().closeSessionInstance();
    }

    /**
     * Convenience method to return {@link NakedObjectSession} for specified
     * {@link NakedObjectSession#getId()}.
     * 
     * <p>
     * Provided primarily for debugging.
     * 
     * @see #getSessionInstance(String)
     */
    public static NakedObjectSession getSession(final String sessionId) {
        return getInstance().getSessionInstance(sessionId);
    }

    /**
     * Convenience method to close all sessions.
     */
    public static void closeAllSessions() {
        LOG.info("closing all instances");
        NakedObjectsContext instance = getInstance();
        if (instance != null) {
        	instance.closeAllSessionsInstance();
        }
    }

    // ///////////////////////////////////////////////////////////
    // Static Convenience methods (application scoped)
    // ///////////////////////////////////////////////////////////

    /**
     * Convenience method returning the {@link NakedObjectSessionFactory} of the current {@link #getSession()
     * session}.
     */
    public static NakedObjectSessionFactory getSessionFactory() {
        return getInstance().getSessionFactoryInstance();
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSessionFactory#getConfiguration()
     */
    public static NakedObjectConfiguration getConfiguration() {
        // REVIEW
        return configuration;
        //return getSessionFactory().getConfiguration();
    }
    
    public static void setConfiguration(NakedObjectConfiguration configuration) {
        NakedObjectsContext.configuration = configuration;
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSessionFactory#getDeploymentType()
     */
    public static DeploymentType getDeploymentType() {
        return getSessionFactory().getDeploymentType();
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSessionFactory#getSpecificationLoader()
     */
    public static SpecificationLoader getSpecificationLoader() {
        return getSessionFactory().getSpecificationLoader();
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSessionFactory#getAuthenticationManager()
     */
    public static AuthenticationManager getAuthenticationManager() {
        return getSessionFactory().getAuthenticationManager();
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSessionFactory#getAuthorizationManager()
     */
	public static AuthorizationManager getAuthorizationManager() {
		return getSessionFactory().getAuthorizationManager();
	}


    /**
     * Convenience method.
     * 
     * @see NakedObjectSessionFactory#getTemplateImageLoader()
     */
    public static TemplateImageLoader getTemplateImageLoader() {
        return getSessionFactory().getTemplateImageLoader();
    }
    
    public static UserProfileLoader getUserProfileLoader() {
        return getSessionFactory().getUserProfileLoader();
    }

    public static List<Object> getServices() {
        return getSessionFactory().getServices();
    }
    

    // ///////////////////////////////////////////////////////////
    // Static Convenience methods (session scoped)
    // ///////////////////////////////////////////////////////////

    public static boolean inSession() {
    	NakedObjectSession session = getInstance().getSessionInstance();
        return session != null;
    }

    /**
     * Convenience method returning the current {@link NakedObjectSession}.
     */
    public static NakedObjectSession getSession() {
        NakedObjectSession session = getInstance().getSessionInstance();
        if (session == null) {
            throw new IllegalStateException("No Session opened for this thread");
        }
        return session;
    }

    /**
     * Convenience method to return the {@link #getSession() current} {@link NakedObjectSession}'s
     * {@link NakedObjectSession#getId() id}.
     * 
     * @see NakedObjectSession#getId()
     */
    public static String getSessionId() {
        return getSession().getId();
    }

    /**
     * @see NakedObjectSession#getAuthenticationSession()
     */
    public static AuthenticationSession getAuthenticationSession() {
        return getSession().getAuthenticationSession();
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSession#getPersistenceSession()
     */
    public static PersistenceSession getPersistenceSession() {
        return getSession().getPersistenceSession();
    }

    /**
     * Convenience method.
     * 
     * @see NakedObjectSession#getUserProfile()
     */
    public static UserProfile getUserProfile() {
        return getSession().getUserProfile();
    }

    /**
     * Convenience methods
     * 
     * @see NakedObjectSession#getPersistenceSession()
     * @see PersistenceSession#getTransactionManager()
     */
    public static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }

    // ///////////////////////////////////////////////////////////
    // Static Convenience methods (transaction scoped)
    // ///////////////////////////////////////////////////////////

    public static boolean inTransaction() {
        return inSession() && 
               getCurrentTransaction() != null && 
               !getCurrentTransaction().getState().isComplete();
    }

    /**
     * Convenience method, returning the current {@link NakedObjectTransaction transaction} (if any).
     * 
     * <p>
     * Transactions are managed using the {@link NakedObjectTransactionManager} obtainable from the
     * {@link NakedObjectSession's} {@link PersistenceSession}.
     * 
     * @see NakedObjectSession#getCurrentTransaction()
     * @see PersistenceSession#getTransactionManager()
     */
    public static NakedObjectTransaction getCurrentTransaction() {
        return getSession().getCurrentTransaction();
    }

    /**
     * Convenience method, returning the {@link MessageBroker} of the {@link #getCurrentTransaction() current
     * transaction}.
     */
    public static MessageBroker getMessageBroker() {
        return getCurrentTransaction().getMessageBroker();
    }

    /**
     * Convenience method, returning the {@link UpdateNotifier} of the {@link #getCurrentTransaction() current
     * transaction}.
     */
    public static UpdateNotifier getUpdateNotifier() {
        return getCurrentTransaction().getUpdateNotifier();
    }

    // ///////////////////////////////////////////////////////////
    // Debug
    // ///////////////////////////////////////////////////////////

    public static DebugInfo[] debugSystem() {
        DebugList debugList = new DebugList("Naked Objects System");
        debugList.add("Context", getInstance());
        debugList.add("Naked Objects session factory", getSessionFactory());
        debugList.add("  Authentication manager", getSessionFactory().getAuthenticationManager());
        debugList.add("  Persistence session factory", getSessionFactory().getPersistenceSessionFactory());
        debugList.add("User profile loader", getUserProfileLoader());

        debugList.add("Reflector", getSpecificationLoader());
        debugList.add("Template image loader", getTemplateImageLoader());

        debugList.add("Deployment type", getDeploymentType().getDebug());
        debugList.add("Configuration", getConfiguration());

        debugList.add("Services", getServices());
        return debugList.debug();
    }
 
    public static DebugInfo[] debugSession() {
        DebugList debugList = new DebugList("Naked Objects Session");
        debugList.add("Naked Objects session", getSession());
        debugList.add("Authentication session", getAuthenticationSession());
        debugList.add("User profile", getUserProfile());
        
        debugList.add("Persistence Session", getPersistenceSession());
        debugList.add("Transaction Manager", getTransactionManager());
        
        debugList.add("Service injector", getPersistenceSession().getServicesInjector());
        debugList.add("Adapter factory", getPersistenceSession().getAdapterFactory());
        debugList.add("Object factory", getPersistenceSession().getObjectFactory());
        debugList.add("OID generator", getPersistenceSession().getOidGenerator());
        debugList.add("Adapter manager", getPersistenceSession().getAdapterManager());
        debugList.add("Services", getPersistenceSession().getServices());
        return debugList.debug();        
    }

    public void debugData(final DebugString debug) {
        debug.appendln("context ", this);
    }

}
// Copyright (c) Naked Objects Group Ltd.
