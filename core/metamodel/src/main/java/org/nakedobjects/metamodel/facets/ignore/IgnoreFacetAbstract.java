package org.nakedobjects.metamodel.facets.ignore;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class IgnoreFacetAbstract extends MarkerFacetAbstract implements IgnoreFacet {

    public static Class<IgnoreFacet> type() {
        return IgnoreFacet.class;
    }

    public IgnoreFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
