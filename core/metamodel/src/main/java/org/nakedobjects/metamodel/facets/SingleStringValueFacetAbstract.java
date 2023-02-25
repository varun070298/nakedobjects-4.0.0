package org.nakedobjects.metamodel.facets;



public abstract class SingleStringValueFacetAbstract extends FacetAbstract implements SingleStringValueFacet {
    private final String value;

    public SingleStringValueFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder, final String value) {
        super(facetType, holder, false);
        this.value = value;
    }

	public String value() {
        return value;
    }

    @Override
    protected String toStringValues() {
        if (value == null) {
            return "null";
        } else {
            return "\"" + value + "\"";
        }
    }

}
