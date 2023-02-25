package org.nakedobjects.metamodel.facets.actions.exploration;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class ExplorationFacetAbstract extends MarkerFacetAbstract implements ExplorationFacet {

    public static Class<? extends Facet> type() {
        return ExplorationFacet.class;
    }

    public ExplorationFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
