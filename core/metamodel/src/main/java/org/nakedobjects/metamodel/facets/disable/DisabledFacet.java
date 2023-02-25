package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.SingleWhenValueFacet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;


/**
 * Disable a property, collection or action.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the member with
 * <tt>@Disabled</tt>.
 */
public interface DisabledFacet extends SingleWhenValueFacet, DisablingInteractionAdvisor {

    /**
     * The reason why the (feature of the) target object is currently disabled, or <tt>null</tt> if enabled.
     */
    public String disabledReason(NakedObject target);
}
