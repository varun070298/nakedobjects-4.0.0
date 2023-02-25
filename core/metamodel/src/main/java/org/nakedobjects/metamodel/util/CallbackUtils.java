package org.nakedobjects.metamodel.util;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.CallbackFacet;

public final class CallbackUtils {
	
	private CallbackUtils() {}

	public static void callCallback(final NakedObject object, final Class<? extends Facet> cls) {
	    final CallbackFacet facet = (CallbackFacet) object.getSpecification().getFacet(cls);
	    if (facet != null) {
	        facet.invoke(object);
	    }
	}

}
