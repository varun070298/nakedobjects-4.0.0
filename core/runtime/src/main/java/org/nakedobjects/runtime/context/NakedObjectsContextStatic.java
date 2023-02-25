package org.nakedobjects.runtime.context;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.system.DeploymentType;


/**
 * Provides <i>access to</i> the current {@link NakedObjectSession} in a single-user {@link DeploymentType
 * deployment} (and thus implemented as a <tt>static</tt> singleton).
 */
public class NakedObjectsContextStatic extends NakedObjectsContext {

    private NakedObjectSession session;

    // //////////////////////////////////////////////
    // Singleton & Constructor
    // //////////////////////////////////////////////

    public static NakedObjectsContext createInstance(final NakedObjectSessionFactory sessionFactory) {
        return new NakedObjectsContextStatic(ContextReplacePolicy.NOT_REPLACEABLE, SessionClosePolicy.EXPLICIT_CLOSE,
                sessionFactory);
    }

    /**
     * Intended for testing; the singleton can be replaced and sessions are autoclosed.
     */
    public static NakedObjectsContext createRelaxedInstance(final NakedObjectSessionFactory sessionFactory) {
        return new NakedObjectsContextStatic(ContextReplacePolicy.REPLACEABLE, SessionClosePolicy.AUTO_CLOSE, sessionFactory);
    }

    protected NakedObjectsContextStatic(
            final ContextReplacePolicy replacePolicy,
            final SessionClosePolicy sessionClosePolicy,
            final NakedObjectSessionFactory sessionFactory) {
        super(replacePolicy, sessionClosePolicy, sessionFactory);
    }

    @Override
    public NakedObjectSession getSessionInstance() {
        return session;
    }

    // //////////////////////////////////////////////
    // open, close
    // //////////////////////////////////////////////

    public NakedObjectSession openSessionInstance(AuthenticationSession authenticationSession) {
    	applySessionClosePolicy();
		session = getSessionFactoryInstance().openSession(authenticationSession);
		session.open();
        return session;
    }

	@Override
    public void doClose() {
        session = null;
    }

    // //////////////////////////////////////////////
    // sessionId(s)
    // //////////////////////////////////////////////

    @Override
    public String[] allSessionIds() {
        return new String[] { getSessionInstance().getId() };
    }

    // //////////////////////////////////////////////
    // Session
    // //////////////////////////////////////////////

    @Override
    public void closeAllSessionsInstance() {
        NakedObjectSession sessionInstance = getSessionInstance();
        if (sessionInstance != null) {
        	sessionInstance.closeAll();
        }
    }

    // //////////////////////////////////////////////
    // Debugging
    // //////////////////////////////////////////////

    public String debugTitle() {
        return "Static Context";
    }

    public void debug(final DebugString debug) {
        debug.appendAsHexln("hash", hashCode());
        session.debugState(debug);
    }

    public void debugAll(final DebugString debug) {
        debug(debug);
        debug.appendln();

        debug(debug, getPersistenceSession());
    }

    private void debug(final DebugString debug, final Object object) {
        if (object instanceof DebugInfo) {
            final DebugInfo d = (DebugInfo) object;
            debug.appendTitle(d.debugTitle());
            d.debugData(debug);
        } else {
            debug.appendln("no debug for " + object);
        }
    }

    @Override
    protected NakedObjectSession getSessionInstance(final String executionContextId) {
        return getSessionInstance();
    }

}
// Copyright (c) Naked Objects Group Ltd.
