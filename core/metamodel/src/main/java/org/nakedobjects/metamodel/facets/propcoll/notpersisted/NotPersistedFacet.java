package org.nakedobjects.metamodel.facets.propcoll.notpersisted;

import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacet;


/**
 * Indicates that a property or a collection shouldn't be persisted.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the property or collection with
 * the <tt>@NotPersisted</tt> annotation.
 * 
 * <p>
 * TODO: need to reconcile with {@link DerivedFacet} that has very similar semantics for properties.
 */
public interface NotPersistedFacet extends MarkerFacet {

}
