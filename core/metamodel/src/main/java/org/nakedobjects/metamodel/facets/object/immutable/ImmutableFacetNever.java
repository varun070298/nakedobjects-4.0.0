package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;


public class ImmutableFacetNever extends ImmutableFacetAbstract {

    public ImmutableFacetNever(final FacetHolder holder) {
        super(When.NEVER, holder);
    }

    /**
     * Always returns <tt>null</tt>.
     */
    @Override
    public String disabledReason(final NakedObject no) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
