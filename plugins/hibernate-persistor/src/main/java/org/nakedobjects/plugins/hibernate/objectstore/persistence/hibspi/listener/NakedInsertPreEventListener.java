package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;


/**
 * Respond to events within Hibernate which need to be reflected within the Naked Objects System.
 */
public class NakedInsertPreEventListener extends NakedEventListenerAbstract
        implements PreInsertEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedInsertPreEventListener.class);

    
    /////////////////////////////////////////////////////////
    // loading
    /////////////////////////////////////////////////////////


    /**
     * If {@link PreInsertEvent#getEntity() pojo} is still
     * {@link ResolveState#TRANSIENT transient}, then changes state of its
     * {@link NakedObject adapter} to {@link ResolveState#UPDATING updating}.
     */
    public boolean onPreInsert(final PreInsertEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("PreInsertEvent " + logString(event));
        }
        final Object entity = event.getEntity();
        final NakedObject adapter = getAdapterFor(entity);
        if (adapter.getResolveState() == ResolveState.TRANSIENT) {
            // need to make sure object won't respond to changes in its state
            // This may have already been done by the PersistAlgorithm, and though this is
            // probably the case it can't be guaranteed
            adapter.changeState(ResolveState.RESOLVED);
            adapter.changeState(ResolveState.UPDATING);
        }
        return false; // object not changed
    }


    /////////////////////////////////////////////////////////
    // Helpers (for logging)
    /////////////////////////////////////////////////////////

    private String logString(final PreInsertEvent event) {
        return event.getEntity().getClass() + " " + event.getId();
    }



}
// Copyright (c) Naked Objects Group Ltd.
