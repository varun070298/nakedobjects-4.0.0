package org.nakedobjects.metamodel.facets.object.bounded;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class BoundedFacetImpl extends BoundedFacetAbstract {

    public BoundedFacetImpl(final FacetHolder holder) {
        super(holder);
    }

    @Override
    public String disabledReason(final NakedObject inObject) {
        return "Bounded";
    }

}

// Copyright (c) Naked Objects Group Ltd.
