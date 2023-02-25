package org.nakedobjects.metamodel.facets.object.dirty;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class DirtyObjectFacetAbstract extends FacetAbstract implements DirtyObjectFacet {

    public DirtyObjectFacetAbstract(final Class<? extends Facet> type, final FacetHolder holder) {
        super(type, holder, false);
    }

}
