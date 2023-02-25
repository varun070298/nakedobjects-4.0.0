package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.IdentityData;

public class OidForServiceResponse implements Encodable, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final IdentityData identityData;
	
	public OidForServiceResponse(IdentityData identityData) {
		this.identityData = identityData;
		instantiated();
	}

	public OidForServiceResponse(DataInputExtended input) throws IOException {
		this.identityData = input.readEncodable(IdentityData.class);
		instantiated();
	}

	public void encode(DataOutputExtended output) throws IOException {
		output.writeEncodable(identityData);
	}

	private void instantiated() {
		// nothing to do
	}

	
	///////////////////////////////////////////
	//
	///////////////////////////////////////////

	public IdentityData getOidData() {
		return identityData;
	}
	
}
