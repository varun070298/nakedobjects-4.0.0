package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.ObjectData;

public class ResolveObjectResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final ObjectData objectData;
	
	public ResolveObjectResponse(ObjectData objectData) {
		this.objectData = objectData;
		instantiated();
	}

	public ResolveObjectResponse(DataInputExtended input) throws IOException {
		this.objectData = input.readEncodable(ObjectData.class);
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeEncodable(objectData);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////

	public ObjectData getObjectData() {
		return objectData;
	}
	
}
