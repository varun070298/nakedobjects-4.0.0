package org.nakedobjects.metamodel.facets.object.callbacks;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


/**
 * Adapter superclass for {@link Facet}s for {@link CallbackFacet}.
 */
public abstract class CallbackFacetAbstract extends FacetAbstract implements CallbackFacet {

    public CallbackFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder) {
        super(facetType, holder, false);
    }

    public abstract void invoke(NakedObject object);

}

// Copyright (c) Naked Objects Group Ltd.
