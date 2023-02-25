package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.applib.adapters.DefaultsProvider;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.util.ClassUtil;


public final class DefaultsProviderUtil {

    private DefaultsProviderUtil() {}

    public static final String DEFAULTS_PROVIDER_NAME_KEY_PREFIX = "nakedobjects.reflector.java.facets.defaulted.";
    public static final String DEFAULTS_PROVIDER_NAME_KEY_SUFFIX = ".providerName";

    static String defaultsProviderNameFromConfiguration(final Class<?> type, final NakedObjectConfiguration configuration) {
        final String key = DEFAULTS_PROVIDER_NAME_KEY_PREFIX + type.getCanonicalName() + DEFAULTS_PROVIDER_NAME_KEY_SUFFIX;
        final String defaultsProviderName = configuration.getString(key);
        return !StringUtils.isEmpty(defaultsProviderName) ? defaultsProviderName : null;
    }

    public static Class<?> defaultsProviderOrNull(final Class<?> candidateClass, final String classCandidateName) {
        final Class<?> type = candidateClass != null ? ClassUtil.implementingClassOrNull(candidateClass.getName(),
                DefaultsProvider.class, FacetHolder.class) : null;
        return type != null ? type : ClassUtil.implementingClassOrNull(classCandidateName, DefaultsProvider.class, FacetHolder.class);
    }

}

// Copyright (c) Naked Objects Group Ltd.
