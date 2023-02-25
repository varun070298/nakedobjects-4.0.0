package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class SetAssociationRequest extends RequestAbstract {
	
    private static final long serialVersionUID = 1L;
    
    private final String fieldIdentifier;
    private final IdentityData target;
    private final IdentityData associate;

    public SetAssociationRequest(
            final AuthenticationSession session,
            final String fieldIdentifier,
            final IdentityData target,
            final IdentityData associate) {
        super(session);
        this.fieldIdentifier = fieldIdentifier;
        this.target = target;
        this.associate = associate;
        initialized();
    }

    public SetAssociationRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.fieldIdentifier = input.readUTF();
        this.target = input.readEncodable(IdentityData.class);
        this.associate = input.readEncodable(IdentityData.class);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeUTF(fieldIdentifier);
        output.writeEncodable(target);
        output.writeEncodable(associate);
    }

	private void initialized() {
		// nothing to do
	}

	
    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public String getFieldIdentifier() {
		return fieldIdentifier;
	}
	
	public IdentityData getTarget() {
		return target;
	}
	
	public IdentityData getAssociate() {
		return associate;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of {@link SetAssociationResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        SetAssociationResponse response = serverFacade.setAssociation(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public SetAssociationResponse getResponse() {
        return (SetAssociationResponse) super.getResponse();
    }
}
// Copyright (c) Naked Objects Group Ltd.
