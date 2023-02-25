package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.SingleStringValueFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.regex.RegExFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Whether the (string) property or a parameter must correspond to a specific mask.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>@Mask</tt> annotation.
 * 
 * <p>
 * TODO: not yet implemented by the framework or any viewer.
 * 
 * @see RegExFacet
 */
public interface MaskFacet extends SingleStringValueFacet, ValidatingInteractionAdvisor {

    /**
     * Whether the provided string matches the mask.
     */
    public boolean doesNotMatch(NakedObject nakedObject);
}
