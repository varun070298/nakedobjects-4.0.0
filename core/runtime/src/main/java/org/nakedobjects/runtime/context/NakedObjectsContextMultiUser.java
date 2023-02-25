package org.nakedobjects.runtime.context;

import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;


/**
 * Provides <i>access to</i> the current {@link NakedObjectSession} in
 * a multi-user deployment.
 */
public abstract class NakedObjectsContextMultiUser extends NakedObjectsContext {

    
    ////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////

    protected NakedObjectsContextMultiUser(NakedObjectSessionFactory sessionFactory) {
        this(ContextReplacePolicy.NOT_REPLACEABLE, SessionClosePolicy.EXPLICIT_CLOSE, sessionFactory);
    }

    protected NakedObjectsContextMultiUser(
            final ContextReplacePolicy contextReplacePolicy, 
            final SessionClosePolicy sessionClosePolicy, 
            final NakedObjectSessionFactory sessionFactory) {
        super(contextReplacePolicy, sessionClosePolicy, sessionFactory);
    }



}
// Copyright (c) Naked Objects Group Ltd.
