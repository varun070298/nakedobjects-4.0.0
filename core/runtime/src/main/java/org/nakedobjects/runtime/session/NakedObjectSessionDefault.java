package org.nakedobjects.runtime.session;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.userprofile.UserProfile;


/**
 * Analogous to a Hibernate <tt>Session</tt>, holds the current set of components for a 
 * specific execution context (such as on a thread).
 * 
 * <p>
 * The <tt>NakedObjectsContext</tt> class (in <tt>nof-core</tt>) is responsible for locating
 * the current execution context.
 * 
 * @see NakedObjectSessionFactory
 */
public class NakedObjectSessionDefault implements NakedObjectSession {

    private static final Logger LOG = Logger.getLogger(NakedObjectSessionDefault.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm:ss,SSS");
    private static int nextId = 1;

    private final NakedObjectSessionFactory executionContextFactory;

    private final AuthenticationSession authenticationSession;
    private PersistenceSession persistenceSession; // only non-final so can be replaced in tests.
    private final UserProfile userProfile;
    
    private final int id;
    private long accessTime;
    private String debugSnapshot;


    public NakedObjectSessionDefault(
            final NakedObjectSessionFactory sessionFactory, 
            final AuthenticationSession authenticationSession,
            final PersistenceSession persistenceSession,
            final UserProfile userProfile) {
        
        // global context
        ensureThatArg(sessionFactory, is(not(nullValue())), "execution context factory is required");

        // session
        ensureThatArg(authenticationSession, is(not(nullValue())), "authentication session is required");
        ensureThatArg(persistenceSession, is(not(nullValue())), "persistence session is required");
       ensureThatArg(userProfile, is(not(nullValue())), "user profile is required");

        this.executionContextFactory = sessionFactory;

        this.authenticationSession = authenticationSession;
        this.persistenceSession = persistenceSession;
        this.userProfile = userProfile;
        
        setSessionOpenTime(System.currentTimeMillis());

        this.id = nextId++;
    }


    ////////////////////////////////////////////////////////
    // open, close
    ////////////////////////////////////////////////////////

    public void open() {
        persistenceSession.open();
    }
    
    /**
     * Closes session.
     */
    public void close() {
        takeSnapshot();
        getPersistenceSession().close();
    }


    ////////////////////////////////////////////////////////
    // shutdown
    ////////////////////////////////////////////////////////

    

    /**
     * Shuts down all components.
     */
    public void closeAll() {
        close();
        
        shutdownIfRequired(persistenceSession);
    }

    private void shutdownIfRequired(Object o) {
        if (o instanceof SessionScopedComponent) {
            SessionScopedComponent requiresSetup = (SessionScopedComponent) o;
            requiresSetup.close();
        }
    }

    ////////////////////////////////////////////////////////
    // ExecutionContextFactory
    ////////////////////////////////////////////////////////
    
    public NakedObjectSessionFactory getSessionFactory() {
        return executionContextFactory;
    }

    /**
     * Convenience method.
     */
    public DeploymentType getDeploymentType() {
        return executionContextFactory.getDeploymentType();
    }

    /**
     * Convenience method.
     */
    public NakedObjectConfiguration getConfiguration() {
        return executionContextFactory.getConfiguration();
    }

    /**
     * Convenience method.
     */
    public SpecificationLoader getSpecificationLoader() {
        return executionContextFactory.getSpecificationLoader();
    }

    /**
     * Convenience method.
     */
    public TemplateImageLoader getTemplateImageLoader() {
        return executionContextFactory.getTemplateImageLoader();
    }

    ////////////////////////////////////////////////////////
    // AuthenticationSession
    ////////////////////////////////////////////////////////

    /**
     * Returns the security session representing this user for this execution context.
     */
    public AuthenticationSession getAuthenticationSession() {
        return authenticationSession;
    }


    private String getSessionUserName() {
        return getAuthenticationSession().getUserName();
    }


    ////////////////////////////////////////////////////////
    // Id
    ////////////////////////////////////////////////////////


    /**
     * Returns an descriptive identifier for this {@link NakedObjectSessionDefault}.
     */
    public String getId() {
        return "#" + id + getSessionUserName();
    }



    ////////////////////////////////////////////////////////
    // Persistence Session
    ////////////////////////////////////////////////////////

    public PersistenceSession getPersistenceSession() {
        return persistenceSession;
    }

    
    ////////////////////////////////////////////////////////
    // Perspective
    ////////////////////////////////////////////////////////

    public UserProfile getUserProfile() {
        return userProfile;
    }

    ////////////////////////////////////////////////////////
    // Session Open Time
    ////////////////////////////////////////////////////////

    protected long getSessionOpenTime() {
        return accessTime;
    }
    
    private void setSessionOpenTime(long accessTime) {
        this.accessTime = accessTime;
    }



    ////////////////////////////////////////////////////////
    // Transaction
    ////////////////////////////////////////////////////////

    /**
     * Convenience method that returns the {@link NakedObjectTransaction} of the session, if any.
     */
    public NakedObjectTransaction getCurrentTransaction() {
        return getTransactionManager().getTransaction();
    }



    ////////////////////////////////////////////////////////
    // testSetObjectPersistor
    ////////////////////////////////////////////////////////


    /**
     * Should only be called in tests. 
     */
    public void testSetObjectPersistor(PersistenceSession objectPersistor) {
        this.persistenceSession = objectPersistor;
    }
    

    ////////////////////////////////////////////////////////
    // toString
    ////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        final ToString asString = new ToString(this);
        asString.append("context", getId());
        appendState(asString);
        return asString.toString();
    }



    ////////////////////////////////////////////////////////
    // Debugging
    ////////////////////////////////////////////////////////

    public void debugAll(final DebugString debug) {
        debug.startSection("Naked Objects Context Snapshot");
        debug.appendln(debugSnapshot);
        debug.endSection();
    }

    public void debug(final DebugString debug) {
        debug.appendAsHexln("hash", hashCode());
        debug.appendln("context id", id);
        debug.appendln("accessed", DATE_FORMAT.format(new Date(getSessionOpenTime())));
        debugState(debug);
    }

    public void takeSnapshot() {
        if (!LOG.isDebugEnabled()) {
            return;
        }
        final DebugString debug = new DebugString();
        debug(debug);
        debug.indent();
        debug.appendln();

        debug(debug, getPersistenceSession());
        if (getCurrentTransaction() != null) {
            debug(debug, getCurrentTransaction().getUpdateNotifier());
            debug(debug, getCurrentTransaction().getMessageBroker());
        }
        debugSnapshot = debug.toString();

        LOG.debug(debugSnapshot);
    }


    private void debug(final DebugString debug, final Object object) {
        if (object instanceof DebugInfo) {
            final DebugInfo d = (DebugInfo) object;
            debug.startSection(d.debugTitle());
            d.debugData(debug);
            debug.endSection();
        } else {
            debug.appendln("no debug for " + object);
        }
    }

    
    public void appendState(final ToString asString) {
        asString.append("authenticationSession", getAuthenticationSession());
        asString.append("persistenceSession", getPersistenceSession());
        asString.append("transaction", getCurrentTransaction());
        if (getCurrentTransaction() != null) {
            asString.append("messageBroker", getCurrentTransaction().getMessageBroker());
            asString.append("updateNotifier", getCurrentTransaction().getUpdateNotifier());
        }
    }

    public void debugState(final DebugString debug) {
        debug.appendln("authenticationSession", getAuthenticationSession());
        debug.appendln("persistenceSession", getPersistenceSession());
        debug.appendln("transaction", getCurrentTransaction());
        if (getCurrentTransaction() != null) {
            debug.appendln("messageBroker", getCurrentTransaction().getMessageBroker());
            debug.appendln("updateNotifier", getCurrentTransaction().getUpdateNotifier());
        }
    }


    ///////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ///////////////////////////////////////////////////////

    private NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }

}
// Copyright (c) Naked Objects Group Ltd.
