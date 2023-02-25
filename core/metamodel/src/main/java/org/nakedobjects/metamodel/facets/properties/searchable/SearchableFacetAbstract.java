package org.nakedobjects.metamodel.facets.properties.searchable;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MultipleValueFacetAbstract;


public abstract class SearchableFacetAbstract extends MultipleValueFacetAbstract implements SearchableFacet {

    public static Class<? extends Facet> type() {
        return SearchableFacet.class;
    }

    private final Class<?> repository;
    private final boolean queryByExample;

    public SearchableFacetAbstract(final Class<?> repository, final boolean queryByExample, final FacetHolder holder) {
        super(type(), holder);
        this.repository = repository;
        this.queryByExample = queryByExample;
    }

    public Class<?> repository() {
        return repository;
    }

    public boolean queryByExample() {
        return queryByExample;
    }

}
