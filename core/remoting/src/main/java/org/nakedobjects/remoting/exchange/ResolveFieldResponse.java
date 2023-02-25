package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.ObjectData;

public class ResolveFieldResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Data data;
	
	public ResolveFieldResponse(Data data) {
		this.data = data;
		instantiated();
	}

	public ResolveFieldResponse(DataInputExtended input) throws IOException {
		this.data = input.readEncodable(ObjectData.class);
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeEncodable(data);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////

	public Data getData() {
		return data;
	}
	
}
