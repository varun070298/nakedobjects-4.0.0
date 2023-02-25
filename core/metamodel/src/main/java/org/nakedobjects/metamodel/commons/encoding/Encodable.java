package org.nakedobjects.metamodel.commons.encoding;

import java.io.IOException;

/**
 * This interface indicates that an object can be encoded into into a byte array so it can be streamed.
 * 
 * <p>
 * By implementing this interface you are agreeing to provide a constructor with a single argument of
 * type {@link DataInputExtended}, which create an instance from the stream.
 */
public interface Encodable {

	
    /**
     * Returns the domain object's value as an encoded byte array via the encoder.
     */
    void encode(DataOutputExtended outputStream) throws IOException;
}

// Copyright (c) Naked Objects Group Ltd.
