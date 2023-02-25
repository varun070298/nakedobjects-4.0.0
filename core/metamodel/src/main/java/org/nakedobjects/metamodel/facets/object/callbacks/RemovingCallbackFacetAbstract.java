package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class RemovingCallbackFacetAbstract extends CallbackFacetAbstract implements RemovingCallbackFacet {

    public static Class<? extends Facet> type() {
        return RemovingCallbackFacet.class;
    }

    public RemovingCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
