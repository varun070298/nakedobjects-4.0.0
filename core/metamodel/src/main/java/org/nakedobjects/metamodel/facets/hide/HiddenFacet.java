package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.SingleWhenValueFacet;
import org.nakedobjects.metamodel.interactions.HidingInteractionAdvisor;


/**
 * Hide a property, collection or action.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the member with
 * <tt>@Hidden</tt>.
 */
public interface HiddenFacet extends SingleWhenValueFacet, HidingInteractionAdvisor {

    /**
     * The reason why the (feature of the) target object is currently hidden, or <tt>null</tt> if visible.
     */
    public String hiddenReason(NakedObject target);

}
