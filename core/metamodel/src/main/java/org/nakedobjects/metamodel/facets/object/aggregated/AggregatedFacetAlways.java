package org.nakedobjects.metamodel.facets.object.aggregated;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public class AggregatedFacetAlways extends MarkerFacetAbstract {

    public AggregatedFacetAlways(final FacetHolder holder) {
        super(AggregatedFacet.class, holder);
    }

}
