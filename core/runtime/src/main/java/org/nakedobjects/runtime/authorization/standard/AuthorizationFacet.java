package org.nakedobjects.runtime.authorization.standard;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;
import org.nakedobjects.metamodel.interactions.HidingInteractionAdvisor;


/**
 * Optionally hide or disable an object, property, collection or action depending on the authorization.
 */
public interface AuthorizationFacet extends Facet, HidingInteractionAdvisor, DisablingInteractionAdvisor {


}
