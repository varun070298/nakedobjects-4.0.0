package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;


public class HiddenFacetImpl extends HiddenFacetAbstract {

    public HiddenFacetImpl(final When value, final FacetHolder holder) {
        super(value, holder);
    }

    public String hiddenReason(final NakedObject targetAdapter) {
        if (value() == When.ALWAYS) {
            return "Always hidden";
        } else if (value() == When.NEVER) {
            return null;
        }

        // remaining tests depend on target in question.
        if (targetAdapter == null) {
            return null;
        }

        if (value() == When.UNTIL_PERSISTED) {
            return targetAdapter.isTransient() ? "Hidden until persisted" : null;
        } else if (value() == When.ONCE_PERSISTED) {
            return targetAdapter.isPersistent() ? "Hidden once persisted" : null;
        }
        return null;
    }

}

// Copyright (c) Naked Objects Group Ltd.
