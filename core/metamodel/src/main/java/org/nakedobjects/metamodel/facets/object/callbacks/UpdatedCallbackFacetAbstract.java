package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class UpdatedCallbackFacetAbstract extends CallbackFacetAbstract implements UpdatedCallbackFacet {

    public static Class<? extends Facet> type() {
        return UpdatedCallbackFacet.class;
    }

    public UpdatedCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
