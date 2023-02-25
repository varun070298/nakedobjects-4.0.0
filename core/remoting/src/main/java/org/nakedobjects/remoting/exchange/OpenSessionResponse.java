package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class OpenSessionResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final AuthenticationSession session;
	
	public OpenSessionResponse(final AuthenticationSession session) {
		this.session = session;
		instantiated();
	}

	public OpenSessionResponse(DataInputExtended input) throws IOException {
		this.session = input.readEncodable(AuthenticationSession.class);
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeEncodable(session);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////
	
	public AuthenticationSession getSession() {
		return session;
	}
	
}
