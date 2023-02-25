package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class ResponseEnvelope implements Encodable, Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final Object object;

    public ResponseEnvelope(final Request request) {
        this(request.getId(), request.getResponse());
    }

    private ResponseEnvelope(final int requestId, final Object responseObject) {
        this.id = requestId;
        this.object = responseObject;
        initialized();
    }

    public ResponseEnvelope(final DataInputExtended input) throws IOException {
    	this.id = input.readInt();
    	this.object = input.readEncodable(Object.class);
    	initialized();
    }
    
    public void encode(final DataOutputExtended output) throws IOException {
        output.writeInt(id);
        output.writeEncodable(object);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    public int getId() {
        return id;
    }

    public Object getObject() {
    	return object;
    }
    
    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("id", id);
        str.append("object", object);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
