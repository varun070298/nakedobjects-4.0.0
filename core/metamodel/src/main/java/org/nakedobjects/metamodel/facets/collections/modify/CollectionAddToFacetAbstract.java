package org.nakedobjects.metamodel.facets.collections.modify;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class CollectionAddToFacetAbstract extends FacetAbstract implements CollectionAddToFacet {

    public static Class<? extends Facet> type() {
        return CollectionAddToFacet.class;
    }

    public CollectionAddToFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    
}
