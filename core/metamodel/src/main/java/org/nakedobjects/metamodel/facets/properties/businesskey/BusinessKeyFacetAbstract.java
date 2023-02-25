package org.nakedobjects.metamodel.facets.properties.businesskey;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;


public abstract class BusinessKeyFacetAbstract extends SingleStringValueFacetAbstract implements BusinessKeyFacet {

    public static Class<? extends Facet> type() {
        return BusinessKeyFacet.class;
    }

    public BusinessKeyFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }

}
