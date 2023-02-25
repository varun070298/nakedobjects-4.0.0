package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class ValueFacetFromConfiguration extends ValueFacetAbstract {

    public ValueFacetFromConfiguration(
    		final String candidateSemanticsProviderName, 
    		final FacetHolder holder, 
    		final NakedObjectConfiguration configuration, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        super(ValueSemanticsProviderUtil.valueSemanticsProviderOrNull(null, candidateSemanticsProviderName), false, holder, configuration, specificationLoader, runtimeContext);
    }

}

// Copyright (c) Naked Objects Group Ltd.
