package org.nakedobjects.metamodel.facets.actions.debug;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class DebugFacetAbstract extends MarkerFacetAbstract implements DebugFacet {

    public static Class<? extends Facet> type() {
        return DebugFacet.class;
    }

    public DebugFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
