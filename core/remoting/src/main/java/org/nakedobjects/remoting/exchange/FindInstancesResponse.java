package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.ObjectData;

public class FindInstancesResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final ObjectData[] instances;
	
	public FindInstancesResponse(ObjectData[] instances) {
		this.instances = instances;
		instantiated();
	}

	public FindInstancesResponse(DataInputExtended input) throws IOException {
		this.instances = input.readEncodables(ObjectData.class);
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeEncodables(instances);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////

	public ObjectData[] getInstances() {
		return instances;
	}
	
}
