package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class ShortWrapperValueSemanticsProvider extends ShortValueSemanticsProviderAbstract {

    private static final Object DEFAULT_VALUE = new Short((short) 0);

    static final Class<?> adaptedClass() {
        return Short.class;
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public ShortWrapperValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public ShortWrapperValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, adaptedClass(), DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

}
// Copyright (c) Naked Objects Group Ltd.
