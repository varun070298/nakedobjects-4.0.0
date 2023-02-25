package org.nakedobjects.metamodel.facets.properties.validate;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class PropertyValidateFacetNone extends PropertyValidateFacetAbstract {

    public PropertyValidateFacetNone(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Returns <tt>null</tt>, ie property is valid.
     * 
     * <p>
     * Subclasses should override as required.
     */
    public String invalidReason(final NakedObject inObject, final NakedObject value) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
