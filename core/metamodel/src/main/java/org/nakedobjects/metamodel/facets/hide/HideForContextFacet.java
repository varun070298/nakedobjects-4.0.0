package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.HidingInteractionAdvisor;


/**
 * Hide a property, collection or action based on the state of the target {@link NakedObject object}.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>hideXxx</tt> support
 * method for the member.
 */
public interface HideForContextFacet extends Facet, HidingInteractionAdvisor {

    public String hiddenReason(NakedObject object);

}

// Copyright (c) Naked Objects Group Ltd.
