package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class DisableForContextFacetNone extends DisableForContextFacetAbstract {

    public DisableForContextFacetNone(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Always returns <tt>null</tt>.
     */
    public String disabledReason(final NakedObject target) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
