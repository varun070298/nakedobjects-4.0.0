package org.nakedobjects.metamodel.facets.collections.modify;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;


/**
 * Clear all objects from a collection.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to either invoking the <tt>clearXxx</tt>
 * support method, or just invoking <tt>clear</tt> on the collection returned by the accessor method.
 */
public interface CollectionClearFacet extends Facet {

    public void clear(NakedObject inObject);
}
