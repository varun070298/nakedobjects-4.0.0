package org.nakedobjects.remoting.data.common;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class NullDataImpl implements NullData, Encodable, Serializable {
	
    private static final long serialVersionUID = 1L;
    private final String type;

    public NullDataImpl(final String type) {
        this.type = type;
        initialized();
    }

    public NullDataImpl(final DataInputExtended input) throws IOException {
        this.type = input.readUTF();
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(type);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////
    

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
