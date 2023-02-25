package org.nakedobjects.metamodel.facets.propparam.validate.maxlength;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.SingleIntValueFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Whether the (string) property or a parameter's length must not exceed a certain length.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>@MaxLength</tt> annotation.
 */
public interface MaxLengthFacet extends SingleIntValueFacet, ValidatingInteractionAdvisor {

    /**
     * Whether the provided string exceeds the maximum length.
     */
    public boolean exceeds(NakedObject nakedObject);

}
