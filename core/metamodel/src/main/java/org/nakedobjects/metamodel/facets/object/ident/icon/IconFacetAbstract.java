package org.nakedobjects.metamodel.facets.object.ident.icon;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class IconFacetAbstract extends FacetAbstract implements IconFacet {

    public static Class<? extends Facet> type() {
        return IconFacet.class;
    }

    public IconFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}
