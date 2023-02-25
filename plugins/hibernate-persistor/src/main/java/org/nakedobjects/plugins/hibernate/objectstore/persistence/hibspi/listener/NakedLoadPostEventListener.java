package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.runtime.persistence.PersistorUtil;


/**
 * Marks {@link NakedObject adapter} as {@link ResolveState#RESOLVED resolved} 
 * (provided it was in state of {@link ResolveState#RESOLVING resolving}) and also clears the 
 * dirty flag on the adapter. 
 * 
 * <p>
 * Occurs after an entity instance is fully loaded.
 */
public class NakedLoadPostEventListener extends NakedEventListenerAbstract
        implements PostLoadEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedLoadPostEventListener.class);

    

    public void onPostLoad(final PostLoadEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("PostLoadEvent " + logString(event));
        }
        final NakedObject adapter = getAdapterManager().getAdapterFor(event.getEntity());
        if (adapter.getResolveState() == ResolveState.RESOLVING) {
            PersistorUtil.end(adapter); // ie RESOLVED
        }
        clearDirtyFor(adapter);
    }



    /////////////////////////////////////////////////////////
    // Helpers (for logging)
    /////////////////////////////////////////////////////////



    private String logString(final PostLoadEvent event) {
        return event.getEntity().getClass() + " " + event.getId();
    }




}
// Copyright (c) Naked Objects Group Ltd.
