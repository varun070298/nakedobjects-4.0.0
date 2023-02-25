package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class ResolveFieldRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final IdentityData target;
    private final String fieldIdentifier;

    public ResolveFieldRequest(final AuthenticationSession session, final IdentityData targetData, final String field) {
        super(session);
        this.target = targetData;
        this.fieldIdentifier = field;
        initialized();
    }

    public ResolveFieldRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.fieldIdentifier = input.readUTF();
        this.target = input.readEncodable(IdentityData.class);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeUTF(fieldIdentifier);
        output.writeEncodable(target);
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
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link ResolveFieldResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        ResolveFieldResponse response = serverFacade.resolveField(this);
		setResponse(response);
    }

    /**
     * Downcasts
     */
    @Override
    public ResolveFieldResponse getResponse() {
        return (ResolveFieldResponse) super.getResponse();
    }

    
    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("target", target);
        str.append("field", fieldIdentifier);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
