package org.nakedobjects.metamodel.facets.ordering.fieldorder;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;


public abstract class FieldOrderFacetAbstract extends SingleStringValueFacetAbstract implements FieldOrderFacet {

    public static Class<? extends Facet> type() {
        return FieldOrderFacet.class;
    }

    public FieldOrderFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }

}
