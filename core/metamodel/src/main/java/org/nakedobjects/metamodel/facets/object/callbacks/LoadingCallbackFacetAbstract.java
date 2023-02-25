package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class LoadingCallbackFacetAbstract extends CallbackFacetAbstract implements LoadingCallbackFacet {

    public static Class<? extends Facet> type() {
        return LoadingCallbackFacet.class;
    }

    public LoadingCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
