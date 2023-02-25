package org.nakedobjects.metamodel.facets.actions.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Obtain defaults for each of the parameters of the action.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>defaultsXxx(...)</tt>
 * support method for an action.
 */
public interface ActionDefaultsFacet extends Facet {

    public abstract Object[] getDefaults(NakedObject inObject);
}
