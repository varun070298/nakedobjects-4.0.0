package org.nakedobjects.metamodel.consent;

import org.nakedobjects.metamodel.facets.Facet;


/**
 * Marker interface for implementations (specifically, {@link Facet}s) that can advise as to whether a member
 * should be disabled.
 * 
 * Used within {@link Allow} and {@link Veto}.
 */
public interface InteractionAdvisor {

    /**
     * For testing purposes only.
     */
    public static InteractionAdvisor NOOP = new InteractionAdvisor() {};

}
