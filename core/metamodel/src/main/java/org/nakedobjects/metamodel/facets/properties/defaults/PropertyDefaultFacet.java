package org.nakedobjects.metamodel.facets.properties.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacet;


/**
 * Provides a default value for a property of a newly created object.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>defaultXxx</tt> supporting method
 * for the property with accessor <tt>getXxx</tt>.
 * 
 * <p>
 * Note: an alternative mechanism may be to specify the value in the created callback.
 * 
 * @see CreatedCallbackFacet
 */
public interface PropertyDefaultFacet extends Facet {

    /**
     * The default value for this property in a newly created object.
     */
    public NakedObject getDefault(NakedObject inObject);
}
