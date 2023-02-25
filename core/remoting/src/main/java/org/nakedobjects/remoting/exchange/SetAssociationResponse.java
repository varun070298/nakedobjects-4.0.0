package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.ObjectData;

public class SetAssociationResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final ObjectData[] updates;
	
	public SetAssociationResponse(ObjectData[] updates) {
		this.updates = updates;
		instantiated();
	}

	public SetAssociationResponse(DataInputExtended input) throws IOException {
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
