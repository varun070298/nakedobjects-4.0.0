package org.nakedobjects.metamodel.facets.object.ident.plural;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;


public abstract class PluralFacetAbstract extends SingleStringValueFacetAbstract implements PluralFacet {

    public static Class<? extends Facet> type() {
        return PluralFacet.class;
    }

    public PluralFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }

}
