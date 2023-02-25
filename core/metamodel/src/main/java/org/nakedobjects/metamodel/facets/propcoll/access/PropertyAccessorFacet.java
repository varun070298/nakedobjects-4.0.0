package org.nakedobjects.metamodel.facets.propcoll.access;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;


/**
 * The mechanism by which the value of the property can be accessed.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the accessor method for a
 * property.
 * 
 * @see PropertySetterFacet
 */
public interface PropertyAccessorFacet extends Facet {

    /**
     * Gets the value of this property from this object.
     */
    public Object getProperty(NakedObject inObject);
}
