package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class MaskFacetAnnotation extends MaskFacetAbstract {
    private final MaskEvaluator evaluator;

    public MaskFacetAnnotation(final String outputMask, String inputMask, final FacetHolder holder) {
        super(outputMask, holder);
        evaluator = inputMask == null ? null : new MaskEvaluator(inputMask);
    }

    public boolean doesNotMatch(final NakedObject nakedObject) {
        if (evaluator == null) {
            return false;
        } else {
            if (nakedObject == null) {
                return false;
            }
            final Object object = nakedObject.getObject();
            if (object == null) {
                return false;
            }
            return !evaluator.evaluate(object.toString());
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
