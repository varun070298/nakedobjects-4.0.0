package org.nakedobjects.metamodel.facets.actions.validate;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * The mechanism by which the set of parameters of the action can be validated before the action itself is
 * invoked.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>validateXxx</tt> support
 * method for an action.
 * 
 * <p>
 * Note that the parameters may be validated independently first (eg a range check on a numeric parameter).
 * 
 * @see ActionInvocationFacet
 */
public interface ActionValidationFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * Reason why the validation has failed, or <tt>null</tt> if okay.
     */
    public String invalidReason(NakedObject target, NakedObject[] arguments);
}
