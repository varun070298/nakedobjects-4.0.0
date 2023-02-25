package org.nakedobjects.metamodel.util;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public final class CollectionFacetUtils {
    private CollectionFacetUtils() {}

    public static CollectionFacet getCollectionFacetFromSpec(final NakedObject objectRepresentingCollection) {
        final NakedObjectSpecification collectionSpec = objectRepresentingCollection.getSpecification();
        return collectionSpec.getFacet(CollectionFacet.class);
    }

    public static int size(final NakedObject collection) {
        final CollectionFacet facet = getCollectionFacetFromSpec(collection);
        return facet.size(collection);
    }

    public static NakedObject firstElement(final NakedObject collection) {
        final CollectionFacet facet = getCollectionFacetFromSpec(collection);
        return facet.firstElement(collection);
    }

    /**
     * @deprecated - use instead {@link #convertToList(NakedObject)}.
     */
    @Deprecated
    public static Object[] convertToArray(final NakedObject collection) {
    	return convertToList(collection).toArray();
    }

    public static List<Object> convertToList(final NakedObject collection) {
        final CollectionFacet facet = getCollectionFacetFromSpec(collection);
        final List<Object> objects = new ArrayList<Object>();
        for(NakedObject adapter: facet.iterable(collection)) {
        	objects.add(adapter.getObject());
        }
        return objects;
    }

    public static List<NakedObject> convertToAdapterList(final NakedObject collection) {
        final CollectionFacet facet = getCollectionFacetFromSpec(collection);
        final List<NakedObject> adapters = new ArrayList<NakedObject>();
        for(NakedObject adapter: facet.iterable(collection)) {
        	adapters.add(adapter);
        }
        return adapters;
    }

}
