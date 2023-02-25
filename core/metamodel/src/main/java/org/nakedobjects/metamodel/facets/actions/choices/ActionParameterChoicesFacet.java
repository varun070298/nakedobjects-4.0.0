package org.nakedobjects.metamodel.facets.actions.choices;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Obtain choices for each of the parameters of the action.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>choicesNXxx</tt> support
 * method for an action (where N is the 0-based parameter number).
 */
public interface ActionParameterChoicesFacet extends Facet {

    public Object[] getChoices(NakedObject inObject);
}

