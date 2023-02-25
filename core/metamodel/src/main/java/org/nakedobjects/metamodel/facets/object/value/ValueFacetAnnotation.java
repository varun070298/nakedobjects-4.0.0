package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.applib.annotation.Value;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class ValueFacetAnnotation extends ValueFacetAbstract {

    private static String semanticsProviderName(final Class<?> annotatedClass, final NakedObjectConfiguration configuration) {
        final Value annotation = (Value) annotatedClass.getAnnotation(Value.class);
        final String semanticsProviderName = annotation.semanticsProviderName();
        if (!StringUtils.isEmpty(semanticsProviderName)) {
            return semanticsProviderName;
        }
        return ValueSemanticsProviderUtil.semanticsProviderNameFromConfiguration(annotatedClass, configuration);
    }

    private static Class<?> semanticsProviderClass(final Class<?> annotatedClass) {
        final Value annotation = (Value) annotatedClass.getAnnotation(Value.class);
        return annotation.semanticsProviderClass();
    }

    public ValueFacetAnnotation(
    		final Class<?> annotatedClass, 
    		final FacetHolder holder, 
    		final NakedObjectConfiguration configuration, 
    		final SpecificationLoader specificationLoader, 
    		final RuntimeContext runtimeContext) {
        this(semanticsProviderName(annotatedClass, configuration), semanticsProviderClass(annotatedClass), holder, configuration, specificationLoader, runtimeContext);
    }

    private ValueFacetAnnotation(
            final String candidateSemanticsProviderName,
            final Class<?> candidateSemanticsProviderClass,
            final FacetHolder holder, 
    		final NakedObjectConfiguration configuration, 
    		final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(ValueSemanticsProviderUtil.valueSemanticsProviderOrNull(candidateSemanticsProviderClass,
                candidateSemanticsProviderName), true, holder, configuration, specificationLoader, runtimeContext);
    }

    /**
     * Always valid, even if the specified semanticsProviderName might have been wrong.
     */
    @Override
    public boolean isValid() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
