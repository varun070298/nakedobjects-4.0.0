package org.nakedobjects.metamodel.facets.propcoll.access;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PropertyAccessorFacetAbstract extends FacetAbstract implements PropertyAccessorFacet {

    public static Class<? extends Facet> type() {
        return PropertyAccessorFacet.class;
    }

    public PropertyAccessorFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public abstract Object getProperty(NakedObject inObject);
}
