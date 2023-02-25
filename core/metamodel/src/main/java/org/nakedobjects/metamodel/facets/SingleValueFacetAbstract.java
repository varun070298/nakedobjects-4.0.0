package org.nakedobjects.metamodel.facets;


public abstract class SingleValueFacetAbstract extends FacetAbstract implements SingleValueFacet {

    public SingleValueFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder) {
        super(facetType, holder, false);
    }

}
