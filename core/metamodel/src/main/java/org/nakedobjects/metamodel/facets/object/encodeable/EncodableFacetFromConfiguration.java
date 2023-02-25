package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class EncodableFacetFromConfiguration extends EncodableFacetAbstract {

    public EncodableFacetFromConfiguration(
    		final String candidateEncoderDecoderName, 
    		final FacetHolder holder, RuntimeContext runtimeContext) {
        super(candidateEncoderDecoderName, null, holder, runtimeContext);

    }
}

// Copyright (c) Naked Objects Group Ltd.
