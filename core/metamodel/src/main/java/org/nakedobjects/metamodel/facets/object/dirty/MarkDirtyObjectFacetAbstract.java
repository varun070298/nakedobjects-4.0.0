package org.nakedobjects.metamodel.facets.object.dirty;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class MarkDirtyObjectFacetAbstract extends DirtyObjectFacetAbstract implements MarkDirtyObjectFacet {

    public static Class<? extends Facet> type() {
        return MarkDirtyObjectFacet.class;
    }

    public MarkDirtyObjectFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
