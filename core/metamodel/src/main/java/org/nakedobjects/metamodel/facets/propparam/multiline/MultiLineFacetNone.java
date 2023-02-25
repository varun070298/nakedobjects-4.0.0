package org.nakedobjects.metamodel.facets.propparam.multiline;

import org.nakedobjects.metamodel.facets.FacetHolder;


public class MultiLineFacetNone extends MultiLineFacetAbstract {

    public MultiLineFacetNone(final boolean preventWrapping, final FacetHolder holder) {
        super(1, preventWrapping, holder);
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
