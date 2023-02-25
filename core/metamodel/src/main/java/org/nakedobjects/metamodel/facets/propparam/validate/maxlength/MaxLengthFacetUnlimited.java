package org.nakedobjects.metamodel.facets.propparam.validate.maxlength;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public class MaxLengthFacetUnlimited extends MaxLengthFacetAbstract {

    public MaxLengthFacetUnlimited(final FacetHolder holder) {
        super(Integer.MAX_VALUE, holder);
    }

    /**
     * No limit to maximum length.
     */
    @Override
    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
