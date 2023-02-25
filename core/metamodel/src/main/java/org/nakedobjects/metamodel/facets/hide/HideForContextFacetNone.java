package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class HideForContextFacetNone extends HideForContextFacetAbstract {

    public HideForContextFacetNone(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Always returns <tt>null</tt>.
     */
    public String hiddenReason(final NakedObject object) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
