package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PersistingCallbackFacetAbstract extends CallbackFacetAbstract implements PersistingCallbackFacet {

    public static Class<? extends Facet> type() {
        return PersistingCallbackFacet.class;
    }

    public PersistingCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
