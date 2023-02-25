package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.HidingInteractionAdvisor;


/**
 * Hide a property, collection or action based on the current session.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>hideXxx(UserMemento)</tt>
 * support method for the member.
 */
public interface HideForSessionFacet extends Facet, HidingInteractionAdvisor {

    public String hiddenReason(AuthenticationSession session);

}

// Copyright (c) Naked Objects Group Ltd.
