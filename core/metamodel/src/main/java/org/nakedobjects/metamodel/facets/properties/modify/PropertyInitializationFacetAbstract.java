package org.nakedobjects.metamodel.facets.properties.modify;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PropertyInitializationFacetAbstract extends FacetAbstract implements PropertyInitializationFacet {

    public static Class<? extends Facet> type() {
        return PropertyInitializationFacet.class;
    }

    public PropertyInitializationFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }
}
