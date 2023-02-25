package org.nakedobjects.metamodel.facets.object.ebc;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class EqualByContentFacetAbstract extends MarkerFacetAbstract implements EqualByContentFacet {

    public static Class<? extends Facet> type() {
        return EqualByContentFacet.class;
    }

    public EqualByContentFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
