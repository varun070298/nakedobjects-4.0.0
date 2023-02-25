package org.nakedobjects.metamodel.java5;

import java.util.List;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.specloader.internal.facetprocessor.FacetProcessor;


/**
 * Indicates that the {@link FacetFactory} works by recognizing methods with a certain prefix (or prefixes).
 * 
 * <p>
 * Used by {@link FacetProcessor#recognizes(java.lang.reflect.Method)}.
 */
public interface MethodPrefixBasedFacetFactory extends FacetFactory {

    /**
     * All prefixes recognized by this {@link FacetFactory}.
     */
    public List<String> getPrefixes();
}

// Copyright (c) Naked Objects Group Ltd.
