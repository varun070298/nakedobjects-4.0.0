package org.nakedobjects.metamodel.facets.object.cached;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;


public abstract class CachedFacetAbstract extends MarkerFacetAbstract implements CachedFacet {

    public static Class<? extends Facet> type() {
        return CachedFacet.class;
    }

    public CachedFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
