package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class RemovedCallbackFacetAbstract extends CallbackFacetAbstract implements RemovedCallbackFacet {

    public static Class<? extends Facet> type() {
        return RemovedCallbackFacet.class;
    }

    public RemovedCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
