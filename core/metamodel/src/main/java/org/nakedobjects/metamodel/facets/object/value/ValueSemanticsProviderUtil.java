package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.applib.adapters.ValueSemanticsProvider;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.util.ClassUtil;


public final class ValueSemanticsProviderUtil {

    private ValueSemanticsProviderUtil() {}

    public static final String SEMANTICS_PROVIDER_NAME_KEY_PREFIX = "nakedobjects.reflector.java.facets.value.";
    public static final String SEMANTICS_PROVIDER_NAME_KEY_SUFFIX = ".semanticsProviderName";

    static String semanticsProviderNameFromConfiguration(final Class<?> type, final NakedObjectConfiguration configuration) {
        final String key = SEMANTICS_PROVIDER_NAME_KEY_PREFIX + type.getCanonicalName() + SEMANTICS_PROVIDER_NAME_KEY_SUFFIX;
        final String semanticsProviderName = configuration.getString(key);
        return !StringUtils.isEmpty(semanticsProviderName) ? semanticsProviderName : null;
    }

    @SuppressWarnings("unchecked")
	public static Class<? extends ValueSemanticsProvider<?>> valueSemanticsProviderOrNull(final Class<?> candidateClass, final String classCandidateName) {
        final Class clazz = candidateClass != null ? ClassUtil.implementingClassOrNull(candidateClass.getName(),
                ValueSemanticsProvider.class, FacetHolder.class) : null;
        return clazz != null ? clazz : ClassUtil.implementingClassOrNull(classCandidateName, ValueSemanticsProvider.class, FacetHolder.class);
    }

}

// Copyright (c) Naked Objects Group Ltd.
