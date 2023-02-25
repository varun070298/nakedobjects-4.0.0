package org.nakedobjects.metamodel.facets.naming.named;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;


public abstract class NamedFacetAbstract extends SingleStringValueFacetAbstract implements NamedFacet {

    public static Class<? extends Facet> type() {
        return NamedFacet.class;
    }

    public NamedFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }
}
