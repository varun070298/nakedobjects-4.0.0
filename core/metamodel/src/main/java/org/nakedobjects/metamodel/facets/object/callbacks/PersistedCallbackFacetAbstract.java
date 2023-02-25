package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PersistedCallbackFacetAbstract extends CallbackFacetAbstract implements PersistedCallbackFacet {

    public static Class<? extends Facet> type() {
        return PersistedCallbackFacet.class;
    }

    public PersistedCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
