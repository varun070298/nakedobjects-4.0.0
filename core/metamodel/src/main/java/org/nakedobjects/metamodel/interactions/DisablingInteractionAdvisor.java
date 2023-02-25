package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.consent.InteractionAdvisorFacet;


/**
 * Mix-in interface for facets that can advise as to whether a member should be disabled.
 * 
 * @see ValidatingInteractionAdvisor
 * @see HidingInteractionAdvisor
 */
public interface DisablingInteractionAdvisor extends InteractionAdvisorFacet {

    /**
     * Whether the rule represented by this facet disables the member to which it applies.
     * 
     * <p>
     * Implementations should use the provided {@link InteractionContext} to determine whether they disable
     * the member from being modified or used. They must however guard against a <tt>null</tt>
     * {@link InteractionContext#getTarget() target} and {@link InteractionContext#getSession() session} -
     * neither are guaranteed to be populated.
     */
    String disables(final UsabilityContext<? extends UsabilityEvent> ic);

}
