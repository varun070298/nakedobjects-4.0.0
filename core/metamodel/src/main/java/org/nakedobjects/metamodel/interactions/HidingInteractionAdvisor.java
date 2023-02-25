package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.metamodel.consent.InteractionAdvisorFacet;


/**
 * Mix-in interface for facets that can advise as to whether a member should be hidden.
 * 
 * @see DisablingInteractionAdvisor
 * @see ValidatingInteractionAdvisor
 */
public interface HidingInteractionAdvisor extends InteractionAdvisorFacet {

    /**
     * Whether the rule represented by this facet hides the member to which it applies.
     * 
     * <p>
     * Implementations should use the provided {@link InteractionContext} to determine whether they declare
     * the object/member is hidden. They must however guard against a <tt>null</tt>
     * {@link InteractionContext#getTarget() target} and {@link InteractionContext#getSession() session} -
     * neither are guaranteed to be populated.
     */
    String hides(final VisibilityContext<? extends VisibilityEvent> ic);
}
