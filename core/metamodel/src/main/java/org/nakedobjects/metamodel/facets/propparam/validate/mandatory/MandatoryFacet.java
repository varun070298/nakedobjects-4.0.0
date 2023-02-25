package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Whether a property or a parameter is mandatory (not optional).
 * 
 * <p>
 * For a mandatory property, the object cannot be saved/updated without the value being provided. For a
 * mandatory parameter, the action cannot be invoked without the value being provided.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, specify mandatory by <i>omitting</i> the
 * <tt>@Optional</tt> annotation.
 */
public interface MandatoryFacet extends MarkerFacet, ValidatingInteractionAdvisor {

    /**
     * Whether this value is required but has not been provided (and is therefore invalid).
     * 
     * <p>
     * If the value has been provided, <i>or</i> if the property or parameter is not required, then will
     * return <tt>false</tt>.
     */
    boolean isRequiredButNull(NakedObject nakedObject);

    /**
     * Indicates that the implementation is overridding the usual semantics, in other words that the
     * {@link FacetHolder} to which this {@link Facet} is attached is <i>not</i> mandatory.
     */
    public boolean isInvertedSemantics();
}
