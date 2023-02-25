package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class DoublePrimitiveValueSemanticsProvider extends DoubleValueSemanticsProviderAbstract implements PropertyDefaultFacet {

    private static final Object DEFAULT_VALUE = new Double(0.0d);

    static final Class<?> adaptedClass() {
        return double.class;
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public DoublePrimitiveValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public DoublePrimitiveValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, adaptedClass(), DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    // //////////////////////////////////////////////////////////////////
    // PropertyDefaultFacet
    // //////////////////////////////////////////////////////////////////

    public NakedObject getDefault(final NakedObject inObject) {
        return createAdapter(double.class, new Double(0.0));
    }

    
}
// Copyright (c) Naked Objects Group Ltd.
