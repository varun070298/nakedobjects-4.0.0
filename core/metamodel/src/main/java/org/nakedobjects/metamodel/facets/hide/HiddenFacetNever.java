package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;


public class HiddenFacetNever extends HiddenFacetAbstract {

    public HiddenFacetNever(final FacetHolder holder) {
        super(When.NEVER, holder);
    }

    /**
     * Always returns <tt>null</tt>.
     */
    public String hiddenReason(final NakedObject target) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

// Copyright (c) Naked Objects Group Ltd.
