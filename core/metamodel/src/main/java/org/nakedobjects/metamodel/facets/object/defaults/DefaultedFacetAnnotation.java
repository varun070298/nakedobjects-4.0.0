package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.applib.annotation.Defaulted;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class DefaultedFacetAnnotation extends DefaultedFacetAbstract {

    private static String providerName(final Class<?> annotatedClass, final NakedObjectConfiguration configuration) {
        final Defaulted annotation = (Defaulted) annotatedClass.getAnnotation(Defaulted.class);
        final String providerName = annotation.defaultsProviderName();
        if (!StringUtils.isEmpty(providerName)) {
            return providerName;
        }
        return DefaultsProviderUtil.defaultsProviderNameFromConfiguration(annotatedClass, configuration);
    }

    private static Class<?> providerClass(final Class<?> annotatedClass) {
        final Defaulted annotation = (Defaulted) annotatedClass.getAnnotation(Defaulted.class);
        return annotation.defaultsProviderClass();
    }

    public DefaultedFacetAnnotation(
            final Class<?> annotatedClass,
            final NakedObjectConfiguration configuration,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        this(providerName(annotatedClass, configuration), providerClass(annotatedClass), holder, runtimeContext);
    }

    private DefaultedFacetAnnotation(
            final String candidateProviderName,
            final Class<?> candidateProviderClass,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        super(candidateProviderName, candidateProviderClass, holder, runtimeContext);
    }

}

// Copyright (c) Naked Objects Group Ltd.
