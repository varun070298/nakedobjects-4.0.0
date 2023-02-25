package org.nakedobjects.metamodel.facets;


public abstract class MarkerFacetAbstract extends FacetAbstract implements MarkerFacet {

    public MarkerFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder) {
        super(facetType, holder, false);
    }

}
