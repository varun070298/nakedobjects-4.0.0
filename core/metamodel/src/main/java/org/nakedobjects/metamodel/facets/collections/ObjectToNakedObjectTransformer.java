package org.nakedobjects.metamodel.facets.collections;

import org.apache.commons.collections.Transformer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;

/**
 * Uses the Commons Collection API to transform {@link Object}s into
 * {@link NakedObject} adapters.
 * 
 */
public final class ObjectToNakedObjectTransformer implements Transformer {
	
	private final RuntimeContext runtimeContext;

	public ObjectToNakedObjectTransformer(final RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}
    
    public Object transform(Object object) {
        return getRuntimeContext().adapterFor(object);
    }
    
    // //////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    // //////////////////////////////////////////////////////////////////

    public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}


}