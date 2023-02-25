package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class DoubleWrapperValueSemanticsProvider extends DoubleValueSemanticsProviderAbstract {

    private static final Object DEFAULT_VALUE = new Double(0.0d);

    static final Class<?> adaptedClass() {
        return Double.class;
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public DoubleWrapperValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public DoubleWrapperValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, adaptedClass(), DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

}
// Copyright (c) Naked Objects Group Ltd.
