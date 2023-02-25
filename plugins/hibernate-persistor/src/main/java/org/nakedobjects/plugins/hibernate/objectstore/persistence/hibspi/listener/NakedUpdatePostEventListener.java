package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.runtime.persistence.PersistorUtil;


/**
 * Respond to events within Hibernate which need to be reflected within the Naked Objects System.
 */
public class NakedUpdatePostEventListener extends NakedEventListenerAbstract
        implements PostUpdateEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedUpdatePostEventListener.class);

    
    public void onPostUpdate(final PostUpdateEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("PostUpdateEvent " + event.getEntity().getClass() + " " + event.getId());
        }
        final NakedObject adapter = getAdapterFor(event.getEntity());
        clearDirtyFor(adapter);
        if (adapter.getResolveState() == ResolveState.UPDATING) {
            PersistorUtil.end(adapter);
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
