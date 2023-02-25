package org.nakedobjects.metamodel.facets.actions.defaults;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class ActionDefaultsFacetAbstract extends FacetAbstract implements ActionDefaultsFacet {

    public static Class<? extends Facet> type() {
        return ActionDefaultsFacet.class;
    }

    public ActionDefaultsFacetAbstract(final FacetHolder holder, boolean derived) {
        super(type(), holder, derived);
    }

}

// Copyright (c) Naked Objects Group Ltd.
