package org.nakedobjects.metamodel.facets;


public abstract class SingleWhenValueFacetAbstract extends FacetAbstract implements SingleWhenValueFacet {
    private final When value;

    public SingleWhenValueFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder, final When value) {
        super(facetType, holder, false);
        this.value = value;
    }

    public When value() {
        return value;
    }

    /**
     * More descriptive, same as {@link #value()}.
     * 
     * @return
     */
    protected When when() {
        return value();
    }

    @Override
    protected String toStringValues() {
        return "when=" + value.getFriendlyName();
    }
}
