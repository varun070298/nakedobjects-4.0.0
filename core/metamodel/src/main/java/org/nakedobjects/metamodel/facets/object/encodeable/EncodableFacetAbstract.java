package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.util.ClassUtil;


public abstract class EncodableFacetAbstract extends FacetAbstract implements EncodableFacet {

    private final Class<?> encoderDecoderClass;

    // to delegate to
    private final EncodableFacetUsingEncoderDecoder encodeableFacetUsingEncoderDecoder;

	private final RuntimeContext runtimeContext;

    public EncodableFacetAbstract(
            final String candidateEncoderDecoderName,
            final Class<?> candidateEncoderDecoderClass,
            final FacetHolder holder, RuntimeContext runtimeContext) {
        super(EncodableFacet.class, holder, false);
        this.runtimeContext = runtimeContext;

        this.encoderDecoderClass = EncoderDecoderUtil.encoderDecoderOrNull(candidateEncoderDecoderClass,
                candidateEncoderDecoderName);
        if (isValid()) {
            EncoderDecoder encoderDecoder = 
                (EncoderDecoder<?>) ClassUtil.newInstance(encoderDecoderClass, FacetHolder.class, holder);
            this.encodeableFacetUsingEncoderDecoder = new EncodableFacetUsingEncoderDecoder(encoderDecoder, holder, getRuntimeContext());
        } else {
            this.encodeableFacetUsingEncoderDecoder = null;
        }
    }

	/**
     * Discover whether either of the candidate encoder/decoder name or class is valid.
     */
    public boolean isValid() {
        return encoderDecoderClass != null;
    }

    /**
     * Guaranteed to implement the {@link EncoderDecoder} class, thanks to generics in the applib.
     */
    public Class<?> getEncoderDecoderClass() {
        return encoderDecoderClass;
    }

    @Override
    protected String toStringValues() {
        return encoderDecoderClass.getName();
    }

    public NakedObject fromEncodedString(final String encodedData) {
        return encodeableFacetUsingEncoderDecoder.fromEncodedString(encodedData);
    }

    public String toEncodedString(final NakedObject object) {
        return encodeableFacetUsingEncoderDecoder.toEncodedString(object);
    }

    
    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}


}

// Copyright (c) Naked Objects Group Ltd.
