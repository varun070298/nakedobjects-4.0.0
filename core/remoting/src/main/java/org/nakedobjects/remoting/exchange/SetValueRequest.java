package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class SetValueRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final String fieldIdentifier;
    private final EncodableObjectData value;
    private final IdentityData target;

    public SetValueRequest(
            final AuthenticationSession session,
            final String fieldIdentifier,
            final IdentityData target,
            final EncodableObjectData value) {
        super(session);
        this.fieldIdentifier = fieldIdentifier;
        this.target = target;
        this.value = value;
        initialized();
    }

    public SetValueRequest(final DataInputExtended input) throws IOException {
        super(input);
        fieldIdentifier = input.readUTF();
        target = input.readEncodable(IdentityData.class);
        value = input.readEncodable(EncodableObjectData.class);
        initialized();        
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
    	
        output.writeUTF(fieldIdentifier);
        output.writeEncodable(target);
        output.writeEncodable(value);
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
	
	public EncodableObjectData getValue() {
		return value;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of {@link SetValueResponse}
	 */
    public void execute(final ServerFacade serverFacade) {
        SetValueResponse response = serverFacade.setValue(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public SetValueResponse getResponse() {
        return (SetValueResponse) super.getResponse();
    }
    
}
// Copyright (c) Naked Objects Group Ltd.
