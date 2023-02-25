package org.nakedobjects.runtime.context;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;


/**
 * A specialised NakedObjectsContext implementation that provides two sets of components: one for the server;
 * and one for the client. This simply determines the current thread and if that thread is the server thread
 * then it provides server data. For any other thread the client data is used.
 */
public class NakedObjectsContextPipe extends NakedObjectsContextMultiUser {
    
    public static NakedObjectsContext createInstance(final NakedObjectSessionFactory sessionFactory) {
        return new NakedObjectsContextPipe(sessionFactory);
    }

    
    private NakedObjectSession clientSession;
    private NakedObjectSession serverSession;
    
    private Thread server;

    
    /////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////
    
    private NakedObjectsContextPipe(final NakedObjectSessionFactory sessionFactory) {
        super(sessionFactory);
    }



    
    /////////////////////////////////////////////////////
    // Server
    /////////////////////////////////////////////////////

    public void setServer(final Thread server) {
        this.server = server;
    }

    private boolean isCurrentThreadServer() {
        return Thread.currentThread() == server;
    }


    /////////////////////////////////////////////////////
    // getCurrent() Hook
    /////////////////////////////////////////////////////

    @Override
    public NakedObjectSession getSessionInstance() {
        if (isCurrentThreadServer()) {
            return serverSession;
        } else {
            return clientSession;
        }
    }
    
    
    public NakedObjectSession openSessionInstance(final AuthenticationSession authenticationSession) {
        applySessionClosePolicy();
        NakedObjectSession newSession = getSessionFactoryInstance().openSession(authenticationSession);
        if (isCurrentThreadServer()) {
            serverSession = newSession;
        } else {
            clientSession = newSession;
        }
        return newSession;
    }




    /////////////////////////////////////////////////////
    // shutdown
    /////////////////////////////////////////////////////


    @Override
    public void closeAllSessionsInstance() {}

    
    /////////////////////////////////////////////////////
    // Execution Context Ids
    /////////////////////////////////////////////////////
    
    @Override
    public String[] allSessionIds() {
        return new String[] { clientSession.getId(), serverSession.getId() };
    }
    
    /////////////////////////////////////////////////////
    // Debugging
    /////////////////////////////////////////////////////

    public String debugTitle() {
        return "Naked Objects (pipe) " + Thread.currentThread().getName();
    }

    @Override
    public void debugData(final DebugString debug) {
        super.debugData(debug);
        debug.appendln("Server thread", server);
    }


    @Override
    protected NakedObjectSession getSessionInstance(final String executionContextId) {
        return null;
    }




}
// Copyright (c) Naked Objects Group Ltd.
