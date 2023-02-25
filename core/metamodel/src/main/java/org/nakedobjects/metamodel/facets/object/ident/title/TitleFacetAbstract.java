package org.nakedobjects.metamodel.facets.object.ident.title;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class TitleFacetAbstract extends FacetAbstract implements TitleFacet {

    public static Class<? extends Facet> type() {
        return TitleFacet.class;
    }

    public TitleFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }
}
