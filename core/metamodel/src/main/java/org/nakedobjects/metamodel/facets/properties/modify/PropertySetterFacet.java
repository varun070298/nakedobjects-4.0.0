package org.nakedobjects.metamodel.facets.properties.modify;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacet;


/**
 * The mechanism by which the value of the property can be set.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the mutator method for a property.
 * 
 * @see PropertyAccessorFacet
 * @see PropertyClearFacet
 * @see PropertyInitializationFacet
 */
public interface PropertySetterFacet extends Facet {

    /**
     * Sets the value of this property.
     */
    public void setProperty(NakedObject inObject, NakedObject value);
}
