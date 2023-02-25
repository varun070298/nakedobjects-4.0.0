package org.nakedobjects.metamodel.facets.actions.choices;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class ActionChoicesFacetNone extends ActionChoicesFacetAbstract {

    public ActionChoicesFacetNone(final FacetHolder holder) {
        super(holder);
    }

    public Object[][] getChoices(final NakedObject inObject) {
        return new NakedObject[0][0];
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
