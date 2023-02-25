package org.nakedobjects.metamodel.facets.ordering.actionorder;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;


public abstract class ActionOrderFacetAbstract extends SingleStringValueFacetAbstract implements ActionOrderFacet {

    public static Class<? extends Facet> type() {
        return ActionOrderFacet.class;
    }

    public ActionOrderFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }

}
