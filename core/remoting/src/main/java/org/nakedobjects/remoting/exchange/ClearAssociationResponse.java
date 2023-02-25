package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.ObjectData;

public class ClearAssociationResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final ObjectData[] updates;
	
	public ClearAssociationResponse(ObjectData[] updates) {
		this.updates = updates;
		instantiated();
	}

	public ClearAssociationResponse(DataInputExtended input) throws IOException {
		this.updates = input.readEncodables(ObjectData.class);
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeEncodables(updates);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////

	public ObjectData[] getUpdates() {
		return updates;
	}
	
}
