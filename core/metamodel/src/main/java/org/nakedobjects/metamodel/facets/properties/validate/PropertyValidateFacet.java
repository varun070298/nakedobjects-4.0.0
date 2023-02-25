package org.nakedobjects.metamodel.facets.properties.validate;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetAbstract;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * The mechanism by which the proposed value of a property can be validated, called immediately before
 * {@link PropertySetterFacetAbstract setting the value}.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>validateXxx</tt> method
 * for a property with an accessor of <tt>getXxx</tt>.
 * 
 * @see PropertySetterFacet
 */

public interface PropertyValidateFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * The reason why the proposed value is invalid.
     * 
     * <p>
     * Should return <tt>null</tt> if the value is in fact valid.
     */
    public String invalidReason(NakedObject targetObject, NakedObject proposedValue);
}
