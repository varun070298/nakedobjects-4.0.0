package org.nakedobjects.metamodel.facets;


public abstract class MultipleValueFacetAbstract extends FacetAbstract implements MultipleValueFacet {

    public MultipleValueFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder) {
        super(facetType, holder, false);
    }

}
