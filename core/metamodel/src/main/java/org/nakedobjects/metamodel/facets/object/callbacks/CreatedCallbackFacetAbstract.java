package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class CreatedCallbackFacetAbstract extends CallbackFacetAbstract implements CreatedCallbackFacet {

    public static Class<? extends Facet> type() {
        return CreatedCallbackFacet.class;
    }

    public CreatedCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
