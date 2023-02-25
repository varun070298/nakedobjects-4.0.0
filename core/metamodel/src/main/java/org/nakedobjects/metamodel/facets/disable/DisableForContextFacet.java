package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;


/**
 * Disable a property, collection or action based on the state of the target {@link NakedObject object}.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>disableXxx</tt> support
 * method for the member.
 */
public interface DisableForContextFacet extends Facet, DisablingInteractionAdvisor {

    /**
     * The reason this object is disabled, or <tt>null</tt> otherwise.
     */
    public String disabledReason(NakedObject object);

}

// Copyright (c) Naked Objects Group Ltd.
