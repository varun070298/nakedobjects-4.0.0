package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class AuthorizationRequestVisibility extends AuthorizationRequestAbstract {
    private static final long serialVersionUID = 1L;

    public AuthorizationRequestVisibility(final AuthenticationSession session, IdentityData targetData, final String data) {
        super(session, targetData, data);
        initialized();
    }
    
    public AuthorizationRequestVisibility(DataInputExtended input) throws IOException {
    	super(input);
    	initialized();
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // execute, resonse
    /////////////////////////////////////////////////////////


	/**
	 * {@link #setResponse(Object) Sets a response} of an {@link AuthorizationResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        AuthorizationResponse response = serverFacade.authorizeVisibility(this);
		setResponse(response);
    }


}
