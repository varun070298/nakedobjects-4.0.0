package org.nakedobjects.metamodel.facets.properties.defaults;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;


public abstract class PropertyDefaultFacetAbstract extends FacetAbstract implements PropertyDefaultFacet {

    public static Class<? extends Facet> type() {
        return PropertyDefaultFacet.class;
    }

    public PropertyDefaultFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

}

// Copyright (c) Naked Objects Group Ltd.
