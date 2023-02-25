package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class DefaultedFacetFromConfiguration extends DefaultedFacetAbstract {

    public DefaultedFacetFromConfiguration(
    		final String candidateProviderName, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(candidateProviderName, null, holder, runtimeContext);

    }
}

// Copyright (c) Naked Objects Group Ltd.
