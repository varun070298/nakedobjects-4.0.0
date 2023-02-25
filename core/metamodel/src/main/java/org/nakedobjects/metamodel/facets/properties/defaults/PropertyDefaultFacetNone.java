package org.nakedobjects.metamodel.facets.properties.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class PropertyDefaultFacetNone extends PropertyDefaultFacetAbstract {

    public PropertyDefaultFacetNone(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Provides a default of <tt>null</tt>.
     */
    public NakedObject getDefault(final NakedObject inObject) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
