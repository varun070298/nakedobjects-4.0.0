package org.nakedobjects.runtime.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;


/**
 * Basic multi-user implementation of NakedObjects that stores a set of components for each thread in use.
 */
public class NakedObjectsContextThreadLocal extends NakedObjectsContextMultiUser {
    
    private static final Logger LOG = Logger.getLogger(NakedObjectsContextThreadLocal.class);

    public static NakedObjectsContext createInstance(final NakedObjectSessionFactory sessionFactory) {
        return new NakedObjectsContextThreadLocal(sessionFactory);
    }

    private final Map<Thread, NakedObjectSession> sessionsByThread = new HashMap<Thread, NakedObjectSession>();

    protected NakedObjectsContextThreadLocal(final NakedObjectSessionFactory sessionFactory) {
        super(sessionFactory);
    }

    
    ///////////////////////////////////////////////////////////
    // Session
    ///////////////////////////////////////////////////////////


    
    @Override
    public void closeAllSessionsInstance() {
        shutdownAllThreads();
    }


    protected void shutdownAllThreads() {
        synchronized (sessionsByThread) {
            int i = 0;
            for (Thread thread: sessionsByThread.keySet()) {
                LOG.info("Shutting down thread: " + i++);
                NakedObjectSession data = sessionsByThread.get(thread);
                data.closeAll();
            }
        }
    }

    
    @Override
    protected void doClose() {
        sessionsByThread.remove(Thread.currentThread());
    }


    ///////////////////////////////////////////////////////////
    // Execution Context Ids
    ///////////////////////////////////////////////////////////

    @Override
    public String[] allSessionIds() {
        final String[] ids = new String[sessionsByThread.size()];
        int i = 0;
        for(Thread thread: sessionsByThread.keySet()) {
            final NakedObjectSession data = sessionsByThread.get(thread);
            ids[i++] = data.getId();
        }
        return ids;
    }


    ///////////////////////////////////////////////////////////
    // Debugging
    ///////////////////////////////////////////////////////////

    public String debugTitle() {
        return "Naked Objects (by thread) " + Thread.currentThread().getName();
    }

    @Override
    public void debugData(final DebugString debug) {
        super.debugData(debug);
        debug.appendln();
        debug.appendTitle("Threads based Contexts");
        for(Thread thread: sessionsByThread.keySet()) {
            final NakedObjectSession data = sessionsByThread.get(thread);
            debug.appendln(thread.toString(), data);
        }
    }

    @Override
    protected NakedObjectSession getSessionInstance(final String executionContextId) {
        for(Thread thread: sessionsByThread.keySet()) {
            final NakedObjectSession data = (NakedObjectSession) sessionsByThread.get(thread);
            if (data.getId().equals(executionContextId)) {
                return data;
            }
        }
        return null;
    }


    ///////////////////////////////////////////////////////////
    // open, close
    ///////////////////////////////////////////////////////////

    

    /**
     * Is only intended to be called through {@link NakedObjectsContext#openSession(AuthenticationSession)}.
     * 
     * <p>
     * Implementation note: an alternative design would have just been to bind onto a thread local.
     */
    @Override
    public NakedObjectSession openSessionInstance(AuthenticationSession authenticationSession) {
        Thread thread = Thread.currentThread();
        synchronized (sessionsByThread) {
            applySessionClosePolicy();
			NakedObjectSession session = getSessionFactoryInstance().openSession(authenticationSession);
			LOG.info("  opening session " + session + " (count " + sessionsByThread.size() + ") for " + authenticationSession.getUserName());
			saveSession(thread, session);
			session.open();
			return session;
        }
    }

	protected NakedObjectSession createAndOpenSession(final Thread thread, AuthenticationSession authenticationSession) {
        NakedObjectSession session = getSessionFactoryInstance().openSession(authenticationSession);
        session.open();
        LOG.info("  opening session " + session + " (count " + sessionsByThread.size() + ") for " + authenticationSession.getUserName());
        return session;
    }

    private NakedObjectSession saveSession(final Thread thread, NakedObjectSession session) {
        synchronized (sessionsByThread) {
            sessionsByThread.put(thread, session);
        }
        LOG.info("  saving session " + session + "; now have " + sessionsByThread.size() + " sessions");
        return session;
    }


    
    ///////////////////////////////////////////////////////////
    // getCurrent()  (Hook)
    ///////////////////////////////////////////////////////////

    /**
     * Get {@link NakedObjectSession execution context} used by the current thread.
     * 
     * <p>
     * If no set exists then throws an {@link IllegalStateException} (must call {@link #openSessionInstance(AuthenticationSession)} first).
     * 
     * @see #openSessionInstance(AuthenticationSession)
     */
    @Override
    public NakedObjectSession getSessionInstance() {
        final Thread thread = Thread.currentThread();
        NakedObjectSession session = (NakedObjectSession) sessionsByThread.get(thread);
        /* REVIEW this has been moved to NakedObjectsContext.getSession()
        if (session == null) {
            throw new IllegalStateException("No Session opened for this thread");
        }*/
        return session;
    }



}
// Copyright (c) Naked Objects Group Ltd.
