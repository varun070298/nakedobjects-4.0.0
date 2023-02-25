package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.util.ClassUtil;


public final class EncoderDecoderUtil {

    private EncoderDecoderUtil() {}

    public static final String ENCODER_DECODER_NAME_KEY_PREFIX = "nakedobjects.reflector.java.facets.encoderDecoder.";
    public static final String ENCODER_DECODER_NAME_KEY_SUFFIX = ".encoderDecoderName";

    static String encoderDecoderNameFromConfiguration(final Class<?> type, final NakedObjectConfiguration configuration) {
        final String key = ENCODER_DECODER_NAME_KEY_PREFIX + type.getCanonicalName() + ENCODER_DECODER_NAME_KEY_SUFFIX;
        final String encoderDecoderName = configuration.getString(key);
        return !StringUtils.isEmpty(encoderDecoderName) ? encoderDecoderName : null;
    }

    public static Class<?> encoderDecoderOrNull(final Class<?> candidateClass, final String classCandidateName) {
        final Class<?> type = candidateClass != null ? ClassUtil.implementingClassOrNull(candidateClass.getName(),
                EncoderDecoder.class, FacetHolder.class) : null;
        return type != null ? type : ClassUtil.implementingClassOrNull(classCandidateName, EncoderDecoder.class, FacetHolder.class);
    }

}

// Copyright (c) Naked Objects Group Ltd.
