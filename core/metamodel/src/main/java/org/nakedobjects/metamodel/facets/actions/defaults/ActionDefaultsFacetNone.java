package org.nakedobjects.metamodel.facets.actions.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class ActionDefaultsFacetNone extends ActionDefaultsFacetAbstract {

    public ActionDefaultsFacetNone(final FacetHolder holder) {
        super(holder, false);
    }

    public Object[] getDefaults(final NakedObject inObject) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
