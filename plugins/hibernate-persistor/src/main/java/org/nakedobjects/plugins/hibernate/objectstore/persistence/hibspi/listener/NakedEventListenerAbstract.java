package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


/**
 * Helper superclass for writing event listeners.
 */
public abstract class NakedEventListenerAbstract {


    /////////////////////////////////////////////////////////
    // Helpers
    /////////////////////////////////////////////////////////

    protected void clearDirtyFor(final NakedObject adapter) {
        adapter.getSpecification().clearDirty(adapter);
    }

    protected NakedObject getAdapterFor(final Object object) {
        return getAdapterManager().getAdapterFor(object);
    }


    /////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////
    
    protected PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    protected AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    

}
// Copyright (c) Naked Objects Group Ltd.
