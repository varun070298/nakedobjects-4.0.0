package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class EncodableFacetUsingEncoderDecoder extends FacetAbstract implements EncodableFacet {

    private final EncoderDecoder encoderDecoder;
	private final RuntimeContext runtimeContext;

    public EncodableFacetUsingEncoderDecoder(
    		final EncoderDecoder encoderDecoder, 
    		final FacetHolder holder, 
    		final RuntimeContext runtimeContext) {
        super(EncodableFacet.class, holder, false);
        this.encoderDecoder = encoderDecoder;
        this.runtimeContext = runtimeContext;
    }

    // TODO: is this safe? really?
    public static String ENCODED_NULL = "NULL";

    @Override
    protected String toStringValues() {
    	getRuntimeContext().injectDependenciesInto(encoderDecoder);
        return encoderDecoder.toString();
    }

    public NakedObject fromEncodedString(final String encodedData) {
        Assert.assertNotNull(encodedData);
        if (ENCODED_NULL.equals(encodedData)) {
            return null;
        } else {
        	getRuntimeContext().injectDependenciesInto(encoderDecoder);
            Object decodedObject = encoderDecoder.fromEncodedString(encodedData);
			return getRuntimeContext().adapterFor(decodedObject);
        }

    }

    public String toEncodedString(final NakedObject object) {
    	getRuntimeContext().injectDependenciesInto(encoderDecoder);
        return object == null ? ENCODED_NULL : encoderDecoder.toEncodedString(object.getObject());
    }


    
    ////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ////////////////////////////////////////////////////////
    

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
