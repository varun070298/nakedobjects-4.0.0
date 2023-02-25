package org.nakedobjects.metamodel.facets.actions.choices;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class ActionChoicesFacetAbstract extends FacetAbstract implements ActionChoicesFacet {

    public static Class<? extends Facet> type() {
        return ActionChoicesFacet.class;
    }

    public ActionChoicesFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}

// Copyright (c) Naked Objects Group Ltd.
