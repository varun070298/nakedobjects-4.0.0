package org.nakedobjects.metamodel.facets.properties.modify;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PropertyClearFacetAbstract extends FacetAbstract implements PropertyClearFacet {

    public static Class<? extends Facet> type() {
        return PropertyClearFacet.class;
    }

    public PropertyClearFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }
}
