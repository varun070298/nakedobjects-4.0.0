package org.nakedobjects.metamodel.facets.propcoll.notpersisted;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class NotPersistedFacetAbstract extends MarkerFacetAbstract implements NotPersistedFacet {

    public static Class<? extends Facet> type() {
        return NotPersistedFacet.class;
    }

    public NotPersistedFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
