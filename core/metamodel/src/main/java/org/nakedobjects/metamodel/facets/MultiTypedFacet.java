package org.nakedobjects.metamodel.facets;

/**
 * A Class that provides multiple facet implementations, either directly or through a delegate.
 * 
 * <p>
 * The client of this interface should use {@link #getFacet(Class)} to obtain the facet implementation for
 * each of the {@link #facetTypes() facets types}.
 */
public interface MultiTypedFacet extends Facet {

    /**
     * All of the facet types either implemented or available by this facet implementation.
     * 
     */
    public Class<? extends Facet>[] facetTypes();

    public <T extends Facet> T getFacet(Class<T> facet);
}

// Copyright (c) Naked Objects Group Ltd.
