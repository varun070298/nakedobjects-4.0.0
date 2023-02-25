package org.nakedobjects.metamodel.facets.object.aggregated;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class AggregatedFacetAbstract extends MarkerFacetAbstract implements AggregatedFacet {

    public static Class<? extends Facet> type() {
        return AggregatedFacet.class;
    }

    public AggregatedFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
