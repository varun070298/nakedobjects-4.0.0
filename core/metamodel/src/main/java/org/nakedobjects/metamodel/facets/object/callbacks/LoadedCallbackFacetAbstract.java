package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class LoadedCallbackFacetAbstract extends CallbackFacetAbstract implements LoadedCallbackFacet {

    public static Class<? extends Facet> type() {
        return LoadedCallbackFacet.class;
    }

    public LoadedCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
