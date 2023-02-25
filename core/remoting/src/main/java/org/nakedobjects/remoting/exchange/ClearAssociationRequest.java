package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.facade.ServerFacade;

/**
 * REVIEW: should we split? the OTMA will need to specify the associate to remove, but the OTOA should not need to provide the associate to clear.
 * @param request TODO
 */
public class ClearAssociationRequest extends RequestAbstract {
	
    private static final long serialVersionUID = 1L;
    private final String fieldIdentifier;
    private final IdentityData target;
    private final IdentityData associate;

    public ClearAssociationRequest(
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

    public ClearAssociationRequest(final DataInputExtended input) throws IOException {
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
	 * {@link #setResponse(Object) Sets a response} of a {@link ClearAssociationResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        ClearAssociationResponse response = serverFacade.clearAssociation(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public ClearAssociationResponse getResponse() {
        return (ClearAssociationResponse) super.getResponse();
    }
    
}
// Copyright (c) Naked Objects Group Ltd.
