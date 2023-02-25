package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.event.InitializeCollectionEvent;
import org.hibernate.event.InitializeCollectionEventListener;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;


public class NakedCollectionInitializeEventListener extends NakedEventListenerAbstract
        implements InitializeCollectionEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedCollectionInitializeEventListener.class);

    
   
    /**
     * Marks the adapters for collections to {@link ResolveState#RESOLVED}
     * (provided that they were {@link ResolveState#isResolvable(ResolveState) resolvable}
     * to {@link ResolveState#RESOLVING}).
     */
    public void onInitializeCollection(final InitializeCollectionEvent event) throws HibernateException {
        LOG.info("InitializeCollectionEvent");
        final NakedObject adapter = getAdapterFor(event.getCollection());
        if (adapter.getResolveState().isResolvable(ResolveState.RESOLVING)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting collection to resolved " + adapter);
            }
            adapter.changeState(ResolveState.RESOLVING);
            adapter.changeState(ResolveState.RESOLVED);
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
