package org.nakedobjects.metamodel.facets;


public abstract class SingleIntValueFacetAbstract extends FacetAbstract implements SingleIntValueFacet {

    private final int value;

    public SingleIntValueFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder, final int value) {
        super(facetType, holder, false);
        this.value = value;
    }

    public int value() {
        return value;
    }
}
