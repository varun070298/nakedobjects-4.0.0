package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class HasInstancesResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean hasInstances;
	
	public HasInstancesResponse(boolean hasInstances) {
		this.hasInstances = hasInstances;
		instantiated();
	}

	public HasInstancesResponse(DataInputExtended input) throws IOException {
		this.hasInstances = input.readBoolean();
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeBoolean(hasInstances);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////
	
	public Boolean hasInstances() {
		return hasInstances;
	}
	
}
