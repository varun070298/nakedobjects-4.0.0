package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.remoting.data.common.IdentityData;

public abstract class AuthorizationRequestAbstract extends RequestAbstract {
    
    private final String dataStr;
    private final IdentityData targetData;

    public AuthorizationRequestAbstract(final AuthenticationSession session, IdentityData targetData, final String data) {
        super(session);
        this.targetData = targetData;
        this.dataStr = data;
        initialized();
    }
    
    public AuthorizationRequestAbstract(final DataInputExtended input) throws IOException {
    	super(input);
    	this.targetData = input.readEncodable(IdentityData.class);
    	this.dataStr = input.readUTF();
    	initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
    	output.writeEncodable(targetData);
    	output.writeUTF(dataStr);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request
    /////////////////////////////////////////////////////////

	public IdentityData getTarget() {
		return targetData;
	}
	
	public String getIdentifier() {
		return dataStr;
	}

    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

    
    /**
     * Downcasts.
     */
    @Override
    public AuthorizationResponse getResponse() {
        return (AuthorizationResponse) super.getResponse();
    }
    
    
}
