package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


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
public class MandatoryFacetDefault extends MandatoryFacetAbstract {

    public MandatoryFacetDefault(final FacetHolder holder) {
        super(holder);
    }

    /**
     * If not specified or, if a string, then zero length.
     */
    public boolean isRequiredButNull(final NakedObject nakedObject) {
        final Object object = unwrapObject(nakedObject);
        if (object == null) {
            return true;
        }
        // special case string handling.
        final String str = unwrapString(nakedObject);
        return str != null && str.length() == 0;
    }

    public boolean isInvertedSemantics() {
        return false;
    }

}
