package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;


public final class AdapterUtils {
    private AdapterUtils() {}

    public static NakedObject createAdapter(final Class<?> type, final Object object, AdapterManager adapterManager, SpecificationLoader specificationLoader) {
	    final NakedObjectSpecification specification = specificationLoader.loadSpecification(type);
	    if (specification.isObject()) {
	        return adapterManager.adapterFor(object);
	    } else {
	        throw new UnknownTypeException("not an object, is this a collection?");
	    }
	}

	public static Object[] getCollectionAsObjectArray(final Object option, final NakedObjectSpecification spec, AdapterManager adapterManager) {
	    final NakedObject collection = adapterManager.adapterFor(option);
	    final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
	    final Object[] optionArray = new Object[facet.size(collection)];
	    int j = 0;
	    for(NakedObject adapter: facet.iterable(collection)) {
			optionArray[j++] = adapter.getObject();
	    }
	    return optionArray;
	}

	public static Object domainObject(final NakedObject inObject) {
	    return inObject == null ? null : inObject.getObject();
	}

}
