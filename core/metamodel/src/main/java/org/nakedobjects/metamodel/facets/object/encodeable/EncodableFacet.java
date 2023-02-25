package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.MultipleValueFacet;


/**
 * Indicates that this class can be encoded/decoded as a string.
 */
public interface EncodableFacet extends MultipleValueFacet {

    /**
     * Equivalent to {@link EncoderDecoder#fromEncodedString(String)}, though may be implemented through some
     * other equivalent mechanism.
     */
    NakedObject fromEncodedString(String encodedData);

    /**
     * Equivalent to {@link EncoderDecoder#toEncodedString(Object)}, though may be implemented through some
     * other equivalent mechanism.
     */
    String toEncodedString(NakedObject object);
}
