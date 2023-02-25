package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


/**
 * Derived by presence of an <tt>@Optional</tt> method.
 * 
 * <p>
 * This implementation indicates that the {@link FacetHolder} is <i>not</i> mandatory, as per
 * {@link #isInvertedSemantics()}.
 */
public class OptionalFacet extends MandatoryFacetAbstract {

    public OptionalFacet(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Always returns <tt>false</tt>, indicating that the facet holder is in fact optional.
     */
    public boolean isRequiredButNull(final NakedObject nakedObject) {
        return false;
    }

    public boolean isInvertedSemantics() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
