package org.nakedobjects.metamodel.facets.collections.modify;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Remove object to a collection.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>removeFromXxx</tt> support
 * method for an action.
 */
public interface CollectionRemoveFromFacet extends Facet {

    public void remove(NakedObject inObject, NakedObject element);
}
