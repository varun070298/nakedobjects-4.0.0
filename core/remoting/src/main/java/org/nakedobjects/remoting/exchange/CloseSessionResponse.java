package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class CloseSessionResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	public CloseSessionResponse() {
		instantiated();
	}

	public CloseSessionResponse(DataInputExtended input) throws IOException {
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////
	
	
}
