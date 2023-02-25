package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;


/**
 * Disable a property, collection or action based on the current session.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the
 * <tt>disableXxx(UserMemento)</tt> support method for the member.
 */
public interface DisableForSessionFacet extends Facet, DisablingInteractionAdvisor {

    /**
     * The reason this is disabled, or <tt>null</tt> if not.
     */
    public String disabledReason(AuthenticationSession session);

}

// Copyright (c) Naked Objects Group Ltd.
