package org.nakedobjects.metamodel.interactions;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.consent.InteractionAdvisorFacet;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Mix-in interface for {@link Facet}s that can advise as to whether a proposed value is valid.
 * 
 * <p>
 * For example, <tt>MaxLengthFacet</tt> does constrain the length of candidate values, whereas
 * <tt>DebugFacet</tt> or <tt>MemberOrderFacet</tt> do not - they are basically just UI hints.
 * 
 * @see DisablingInteractionAdvisor
 * @see HidingInteractionAdvisor
 */
public interface ValidatingInteractionAdvisor extends InteractionAdvisorFacet {

    /**
     * Whether the validation represented by this facet passes or fails.
     * 
     * <p>
     * Implementations should use the provided {@link ValidityContext} to determine whether they declare the
     * interaction invalid. They must however guard against a <tt>null</tt>
     * {@link ValidityContext#getTarget() target} and {@link ValidityContext#getSession() session} - neither
     * are guaranteed to be populated.
     */
    String invalidates(final ValidityContext<? extends ValidityEvent> ic);
}
