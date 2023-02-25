package org.nakedobjects.metamodel.facets.collections.modify;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class CollectionRemoveFromFacetAbstract extends FacetAbstract implements CollectionRemoveFromFacet {

    public static Class<? extends Facet> type() {
        return CollectionRemoveFromFacet.class;
    }

    public CollectionRemoveFromFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}
