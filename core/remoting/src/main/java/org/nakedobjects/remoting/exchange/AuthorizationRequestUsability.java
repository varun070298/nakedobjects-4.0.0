package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class AuthorizationRequestUsability extends AuthorizationRequestAbstract {
    private static final long serialVersionUID = 1L;
    
    public AuthorizationRequestUsability(
    		final AuthenticationSession session, IdentityData targetData, final String dataStr) {
        super(session, targetData, dataStr);
        initialized();
    }
    
    public AuthorizationRequestUsability(final DataInputExtended input) throws IOException {
    	super(input);
    	initialized();
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

    public void execute(final ServerFacade serverFacade) {
        AuthorizationResponse response = serverFacade.authorizeUsability(this);
		setResponse(response);
    }
    
    
}
