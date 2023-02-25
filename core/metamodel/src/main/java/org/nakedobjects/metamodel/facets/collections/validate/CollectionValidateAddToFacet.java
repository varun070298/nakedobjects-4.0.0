package org.nakedobjects.metamodel.facets.collections.validate;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Validate that an object can be added to a collection.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>validateAddToXxx</tt>
 * support method for a collection.
 * 
 */
public interface CollectionValidateAddToFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * Reason the object cannot be added, or <tt>null</tt> if okay.
     */
    public String invalidReason(NakedObject target, NakedObject proposedArgument);
}

// Copyright (c) Naked Objects Group Ltd.
