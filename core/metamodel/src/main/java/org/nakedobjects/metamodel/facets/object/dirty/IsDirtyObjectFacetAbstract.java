package org.nakedobjects.metamodel.facets.object.dirty;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class IsDirtyObjectFacetAbstract extends DirtyObjectFacetAbstract implements IsDirtyObjectFacet {

    public static Class<? extends Facet> type() {
        return IsDirtyObjectFacet.class;
    }

    public IsDirtyObjectFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

}
