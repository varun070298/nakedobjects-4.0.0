package org.nakedobjects.metamodel.facets.actions.defaults;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class ActionParameterDefaultsFacetAbstract extends FacetAbstract implements ActionParameterDefaultsFacet {

    public static Class<? extends Facet> type() {
        return ActionParameterDefaultsFacet.class;
    }

    public ActionParameterDefaultsFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}

