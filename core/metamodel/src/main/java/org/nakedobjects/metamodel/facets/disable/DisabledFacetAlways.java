package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;


public class DisabledFacetAlways extends DisabledFacetAbstract {

    public DisabledFacetAlways(final FacetHolder holder) {
        super(When.ALWAYS, holder);
    }

    /**
     * Always returns <i>Always disabled</i>.
     */
    public String disabledReason(final NakedObject target) {
        return "Always disabled";
    }

}

// Copyright (c) Naked Objects Group Ltd.
