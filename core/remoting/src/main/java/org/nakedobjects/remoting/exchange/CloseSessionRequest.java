package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.remoting.facade.ServerFacade;

public class CloseSessionRequest extends RequestAbstract {

    private static final long serialVersionUID = 1L;

    public CloseSessionRequest(final AuthenticationSession session) {
    	super(session);
    	initialized();
    }
    
    public CloseSessionRequest(final DataInputExtended input) throws IOException {
        super(input);
        initialized();
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} to {@link CloseSessionResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        CloseSessionResponse response = serverFacade.closeSession(this);
        setResponse(response);
    }

    
    /**
     * Downcasts.
     */
    @Override
    public CloseSessionResponse getResponse() {
    	return (CloseSessionResponse) super.getResponse();
    }
    
    
}

// Copyright (c) Naked Objects Group Ltd.
