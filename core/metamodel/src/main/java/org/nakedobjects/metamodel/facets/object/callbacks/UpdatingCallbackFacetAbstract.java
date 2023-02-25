package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class UpdatingCallbackFacetAbstract extends CallbackFacetAbstract implements UpdatingCallbackFacet {

    public static Class<? extends Facet> type() {
        return UpdatingCallbackFacet.class;
    }

    public UpdatingCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
