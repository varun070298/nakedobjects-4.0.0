package org.nakedobjects.metamodel.util;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public final class NakedObjectAdapterUtils {
    private NakedObjectAdapterUtils() {}

    public static Object[] getCollectionAsObjectArray(final Object option, final NakedObjectSpecification spec, RuntimeContext runtimeContext) {
        final NakedObject collection = runtimeContext.adapterFor(option);
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
    	final Object[] optionArray = new Object[facet.size(collection)];
    	int j = 0;
        for(NakedObject nextElement: facet.iterable(collection)) {
			optionArray[j++] = nextElement.getObject();
        }
        return optionArray;
    }

}
