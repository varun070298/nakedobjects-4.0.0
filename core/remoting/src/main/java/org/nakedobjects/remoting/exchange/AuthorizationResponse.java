package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class AuthorizationResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean authorized;
	
	public AuthorizationResponse(boolean authorized) {
		this.authorized = authorized;
		instantiated();
	}

	public AuthorizationResponse(DataInputExtended input) throws IOException {
		this.authorized = input.readBoolean();
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeBoolean(authorized);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////
	
	public Boolean isAuthorized() {
		return authorized;
	}
	
}
