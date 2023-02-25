package org.nakedobjects.metamodel.facets;


/**
 * Convenience abstract implementation of {@link MultiTypedFacet}.
 */
public abstract class MultiTypedFacetAbstract extends FacetAbstract implements MultiTypedFacet {

    private final Class<? extends Facet>[] facetTypes;

    public MultiTypedFacetAbstract(final Class<? extends Facet> facetType, final Class<? extends Facet>[] facetTypes, final FacetHolder holder) {
        super(facetType, holder, false);
        this.facetTypes = facetTypes;
    }

    public final Class<? extends Facet>[] facetTypes() {
        return facetTypes;
    }

    public abstract <T extends Facet> T getFacet(Class<T> facetType);

}

// Copyright (c) Naked Objects Group Ltd.
