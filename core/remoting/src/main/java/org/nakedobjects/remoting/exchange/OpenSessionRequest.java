package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.facade.ServerFacade;

public class OpenSessionRequest extends RequestAbstract {
	
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;

    public OpenSessionRequest(final String username, final String password) {
        super((AuthenticationSession) null);
        this.username = username;
        this.password = password;
        initialized();
    }

    public OpenSessionRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.username = input.readUTF();
        this.password = input.readUTF();
        initialized();
    }

    @Override
	public void encode(final DataOutputExtended output) throws IOException {
    	super.encode(output);
    	output.writeUTF(username);
    	output.writeUTF(password);
    }

	private void initialized() {
		// nothing to do
	}

	
    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////


	/**
	 * {@link #setResponse(Object) Sets a response} of an {@link AuthenticationSession}.
	 */
    public void execute(final ServerFacade serverFacade) {
        OpenSessionResponse response = serverFacade.openSession(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public OpenSessionResponse getResponse() {
        return (OpenSessionResponse) super.getResponse();
    }

    
    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("sequence", getId());
        return str.toString();
    }


}

// Copyright (c) Naked Objects Group Ltd.
