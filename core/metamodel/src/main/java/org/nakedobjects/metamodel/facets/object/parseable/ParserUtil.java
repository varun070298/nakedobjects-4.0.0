package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.util.ClassUtil;


public final class ParserUtil {

    private ParserUtil() {}

    public static final String PARSER_NAME_KEY_PREFIX = "nakedobjects.reflector.java.facets.parser.";
    public static final String PARSER_NAME_KEY_SUFFIX = ".parserName";

    static String parserNameFromConfiguration(final Class<?> type, final NakedObjectConfiguration configuration) {
        final String key = PARSER_NAME_KEY_PREFIX + type.getCanonicalName() + PARSER_NAME_KEY_SUFFIX;
        final String parserName = configuration.getString(key);
        return !StringUtils.isEmpty(parserName) ? parserName : null;
    }

    @SuppressWarnings("unchecked")
	public static Class<? extends Parser<?>> parserOrNull(final Class<?> candidateClass, final String classCandidateName) {
        final Class type = candidateClass != null ? ClassUtil.implementingClassOrNull(candidateClass.getName(), Parser.class, FacetHolder.class)
                : null;
        return type != null ? type : ClassUtil.implementingClassOrNull(classCandidateName, Parser.class, FacetHolder.class);
    }

}

// Copyright (c) Naked Objects Group Ltd.
