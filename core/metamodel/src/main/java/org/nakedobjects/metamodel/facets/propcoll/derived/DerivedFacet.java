package org.nakedobjects.metamodel.facets.propcoll.derived;

import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.facets.propcoll.notpersisted.NotPersistedFacet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;


/**
 * Indicates that a property is derived and so shouldn't be persisted.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds providing only an accessor method but no
 * publicly-visible modifier methods.
 * 
 * @see NotPersistedFacet
 */
public interface DerivedFacet extends MarkerFacet, DisablingInteractionAdvisor {

}
