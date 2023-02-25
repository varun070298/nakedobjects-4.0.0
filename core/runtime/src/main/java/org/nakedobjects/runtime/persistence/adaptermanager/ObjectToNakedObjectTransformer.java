package org.nakedobjects.runtime.persistence.adaptermanager;

import org.apache.commons.collections.Transformer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;

/**
 * Uses the Commons Collection API to transform {@link Object}s into
 * {@link NakedObject} adapters.
 * 
 */
public final class ObjectToNakedObjectTransformer implements Transformer {
	
	public ObjectToNakedObjectTransformer() {}
    
    public Object transform(Object object) {
        return getAdapterManager().adapterFor(object);
    }
    
    // //////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    // //////////////////////////////////////////////////////////////////
    
    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }


}