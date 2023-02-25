package org.nakedobjects.metamodel.facets.collections.modify;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Add object to a collection.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>addToXxx</tt> support
 * method for a collection.
 */
public interface CollectionAddToFacet extends Facet {

    public void add(NakedObject inObject, NakedObject value);

}
