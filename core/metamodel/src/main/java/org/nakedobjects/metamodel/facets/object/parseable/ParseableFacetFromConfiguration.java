package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class ParseableFacetFromConfiguration extends ParseableFacetAbstract {

    public ParseableFacetFromConfiguration(
    		final String candidateParserName, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(candidateParserName, null, holder, runtimeContext);

    }
}

// Copyright (c) Naked Objects Group Ltd.
