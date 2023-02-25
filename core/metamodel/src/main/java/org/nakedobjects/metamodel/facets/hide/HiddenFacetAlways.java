package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;


public class HiddenFacetAlways extends HiddenFacetAbstract {

    public HiddenFacetAlways(final FacetHolder holder) {
        super(When.ALWAYS, holder);
    }

    /**
     * Always returns <i>Always hidden</i>.
     */
    public String hiddenReason(final NakedObject target) {
        return "Always hidden";
    }

}

// Copyright (c) Naked Objects Group Ltd.
