package org.nakedobjects.metamodel.facets.object.dirty;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class ClearDirtyObjectFacetAbstract extends DirtyObjectFacetAbstract implements ClearDirtyObjectFacet {

    public static Class<? extends Facet> type() {
        return ClearDirtyObjectFacet.class;
    }

    public ClearDirtyObjectFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
