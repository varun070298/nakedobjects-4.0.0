package org.nakedobjects.metamodel.facets.properties.modify;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacet;


/**
 * The mechanism by which the value of the property can be initialised.
 * 
 * <p>
 * This differs from the {@link PropertySetterFacet} in that it is only called when object is set up (after
 * persistence) and not every time a property changes; hence it will not be made part of a transaction.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the mutator method for a property.
 * 
 * @see PropertyAccessorFacet
 * @see PropertySetterFacet
 * @see PropertyClearFacet
 */
public interface PropertyInitializationFacet extends Facet {

    /**
     * Sets the value of this property.
     */
    public void initProperty(NakedObject inObject, NakedObject value);
}
