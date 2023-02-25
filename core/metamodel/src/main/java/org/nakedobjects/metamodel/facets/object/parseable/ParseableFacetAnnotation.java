package org.nakedobjects.metamodel.facets.object.parseable;

import org.nakedobjects.applib.annotation.Parseable;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class ParseableFacetAnnotation extends ParseableFacetAbstract {

    private static String parserName(final Class<?> annotatedClass, final NakedObjectConfiguration configuration) {
        final Parseable annotation = (Parseable) annotatedClass.getAnnotation(Parseable.class);
        final String parserName = annotation.parserName();
        if (!StringUtils.isEmpty(parserName)) {
            return parserName;
        }
        return ParserUtil.parserNameFromConfiguration(annotatedClass, configuration);
    }

    private static Class<?> parserClass(final Class<?> annotatedClass) {
        final Parseable annotation = (Parseable) annotatedClass.getAnnotation(Parseable.class);
        return annotation.parserClass();
    }

    public ParseableFacetAnnotation(
            final Class<?> annotatedClass,
            final NakedObjectConfiguration configuration,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        this(parserName(annotatedClass, configuration), parserClass(annotatedClass), holder, runtimeContext);
    }

    private ParseableFacetAnnotation(
    		final String candidateParserName, 
    		final Class<?> candidateParserClass, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(candidateParserName, candidateParserClass, holder, runtimeContext);
    }

}

// Copyright (c) Naked Objects Group Ltd.
