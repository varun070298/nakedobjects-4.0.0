package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class BooleanWrapperValueSemanticsProvider extends BooleanValueSemanticsProviderAbstract {

    private static final Object DEFAULT_PROVIDER = Boolean.FALSE;

    static final Class<?> adaptedClass() {
        return Boolean.class;
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public BooleanWrapperValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public BooleanWrapperValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, adaptedClass(), DEFAULT_PROVIDER, configuration, specificationLoader, runtimeContext);
    }

    // //////////////////////////////////////////////////////////////////
    // BooleanValueFacet impl
    // //////////////////////////////////////////////////////////////////

    public void reset(final NakedObject object) {
        object.replacePojo(Boolean.FALSE);
    }

    public void set(final NakedObject object) {
        object.replacePojo(Boolean.TRUE);
    }

    public void toggle(final NakedObject object) {
        final Object currentObj = object.getObject();
        if (currentObj == null) {
            set(object);
        }
        final boolean current = ((Boolean) currentObj).booleanValue();
        final boolean toggled = !current;
        object.replacePojo(Boolean.valueOf(toggled));
    }

}
// Copyright (c) Naked Objects Group Ltd.
