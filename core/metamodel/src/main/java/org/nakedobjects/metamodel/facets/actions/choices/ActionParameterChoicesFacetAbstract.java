package org.nakedobjects.metamodel.facets.actions.choices;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class ActionParameterChoicesFacetAbstract extends FacetAbstract implements ActionParameterChoicesFacet {

    public static Class<? extends Facet> type() {
        return ActionParameterChoicesFacet.class;
    }

    public ActionParameterChoicesFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}

// Copyright (c) Naked Objects Group Ltd.

