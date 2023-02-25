package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.applib.annotation.Encodable;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class EncodableFacetAnnotation extends EncodableFacetAbstract {

    private static String encoderDecoderName(final Class<?> annotatedClass, final NakedObjectConfiguration configuration) {
        final Encodable annotation = (Encodable) annotatedClass.getAnnotation(Encodable.class);
        final String encoderDecoderName = annotation.encoderDecoderName();
        if (!StringUtils.isEmpty(encoderDecoderName)) {
            return encoderDecoderName;
        }
        return EncoderDecoderUtil.encoderDecoderNameFromConfiguration(annotatedClass, configuration);
    }

    private static Class<?> encoderDecoderClass(final Class<?> annotatedClass) {
        final Encodable annotation = (Encodable) annotatedClass.getAnnotation(Encodable.class);
        return annotation.encoderDecoderClass();
    }

    public EncodableFacetAnnotation(
            final Class<?> annotatedClass,
            final NakedObjectConfiguration configuration,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        this(encoderDecoderName(annotatedClass, configuration), encoderDecoderClass(annotatedClass), holder, runtimeContext);
    }

    private EncodableFacetAnnotation(
            final String candidateEncoderDecoderName,
            final Class<?> candidateEncoderDecoderClass,
            final FacetHolder holder, 
            final RuntimeContext runtimeContext) {
        super(candidateEncoderDecoderName, candidateEncoderDecoderClass, holder, runtimeContext);
    }

}

// Copyright (c) Naked Objects Group Ltd.
