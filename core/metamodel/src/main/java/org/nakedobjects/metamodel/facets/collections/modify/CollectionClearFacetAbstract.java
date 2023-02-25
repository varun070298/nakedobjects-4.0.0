package org.nakedobjects.metamodel.facets.collections.modify;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class CollectionClearFacetAbstract extends FacetAbstract implements CollectionClearFacet {

    public static Class<? extends Facet> type() {
        return CollectionClearFacet.class;
    }

    public CollectionClearFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}
