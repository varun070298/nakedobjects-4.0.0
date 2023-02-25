package org.nakedobjects.metamodel.facets.object.validate;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.PersistingCallbackFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.UpdatingCallbackFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Mechanism for determining whether this object is in a valid state, for example so that it can be persisted
 * or updated.
 * 
 * <p>
 * Even though all the properties of an object may themselves be valid, there could be inter-property
 * dependencies which are invalid. For example <tt>fromDate</tt> > <tt>toDate</tt> would probably represent an
 * invalid state.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to the <tt>validate</tt> method.
 * 
 * @see PersistingCallbackFacet
 * @see UpdatingCallbackFacet
 */
public interface ValidateObjectFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * The reason the object is invalid.
     * 
     * <p>
     * . If the object is actually valid, should return <tt>null</tt>.
     */
    public String invalidReason(NakedObject nakedObject);

}
