package org.nakedobjects.runtime.persistence.services;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;


public class RepositoryHelper {

    public static Object[] allInstances(final Class<?> cls) {
        return allInstances(getSpecificationLoader().loadSpecification(cls), cls);
    }

    public static Object[] allInstances(final NakedObjectSpecification spec, final Class<?> cls) {
        final NakedObject instances = getPersistenceSession().findInstances(new QueryFindAllInstances(spec), QueryCardinality.MULTIPLE);
        final Object[] array = convertToArray(instances, cls);
        return array;
    }

    private static List<Object> convertToList(final NakedObject instances, final Class<?> cls) {
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(instances);
        final List<Object> list = new ArrayList<Object>();
        for(final NakedObject adapter: facet.iterable(instances)) {
            list.add(adapter.getObject());
        }
        return list;
    }

    private static Object[] convertToArray(final NakedObject instances, final Class<?> cls) {
        return convertToList(instances, cls).toArray();
    }

    public static List<Object> findByPersistenceQuery(final PersistenceQuery persistenceQuery, final Class<?> cls) {
		final NakedObject instances = getPersistenceSession().findInstances(persistenceQuery);
        return convertToList(instances, cls);

    }

    public static List<Object> findByTitle(final Class<?> type, final String title) {
        return findByTitle(getSpecificationLoader().loadSpecification(type), type, title);
    }

    public static List<Object> findByTitle(
            final NakedObjectSpecification spec,
            final Class<?> cls,
            final String title) {
        final PersistenceQuery criteria = new PersistenceQueryFindByTitle(spec, title);
        return findByPersistenceQuery(criteria, cls);
    }

    public static boolean hasInstances(final Class<?> type) {
        return hasInstances(getSpecificationLoader().loadSpecification(type));
    }

    public static boolean hasInstances(final NakedObjectSpecification spec) {
        return getPersistenceSession().hasInstances(spec);
    }

	private static PersistenceSession getPersistenceSession() {
		return NakedObjectsContext.getPersistenceSession();
	}

	private static SpecificationLoader getSpecificationLoader() {
		return NakedObjectsContext.getSpecificationLoader();
	}


}
// Copyright (c) Naked Objects Group Ltd.
