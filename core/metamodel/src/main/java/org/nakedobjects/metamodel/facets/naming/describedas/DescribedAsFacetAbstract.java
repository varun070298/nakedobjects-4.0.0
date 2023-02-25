package org.nakedobjects.metamodel.facets.naming.describedas;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;


public abstract class DescribedAsFacetAbstract extends SingleStringValueFacetAbstract implements DescribedAsFacet {

    public static Class<? extends Facet> type() {
        return DescribedAsFacet.class;
    }

    public DescribedAsFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }

}
