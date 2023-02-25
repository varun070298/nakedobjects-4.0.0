package org.nakedobjects.metamodel.facets.collections.validate;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Validate that an object can be removed to a collection.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>validateRemoveFromXxx</tt>
 * support method for a collection.
 */
public interface CollectionValidateRemoveFromFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * Reason the object cannot be removed, or <tt>null</tt> if okay.
     */
    public String invalidReason(NakedObject inObject, NakedObject value);
}

// Copyright (c) Naked Objects Group Ltd.
