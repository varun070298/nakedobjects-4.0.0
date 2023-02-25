package org.nakedobjects.remoting.data.common;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class EncodableObjectDataImpl implements EncodableObjectData, Encodable, Serializable {
	
    private static final long serialVersionUID = 1L;
    private final String type;
    private final String objectAsString;

    public EncodableObjectDataImpl(final String type, final String objectAsString) {
        this.type = type == null ? null : type.equals(String.class.getName()) ? "s" : type;
        this.objectAsString = objectAsString;
        initialized();
    }

    public EncodableObjectDataImpl(final DataInputExtended input) throws IOException {
        this.type = input.readUTF();
        this.objectAsString = input.readUTF();
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(type);
        output.writeUTF(objectAsString);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    

    public String getEncodedObjectData() {
        return objectAsString;
    }

    public String getType() {
        return type.equals("s") ? String.class.getName() : type;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        str.append("value", objectAsString);
        return str.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
