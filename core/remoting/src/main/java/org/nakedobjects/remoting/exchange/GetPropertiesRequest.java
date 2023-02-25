package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.util.Properties;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.remoting.facade.ServerFacade;

public class GetPropertiesRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;

    public GetPropertiesRequest() {
        super((AuthenticationSession) null);
        initialized();
    }

    public GetPropertiesRequest(final DataInputExtended input) throws IOException {
        super(input);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
    }
    
	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link GetPropertiesResponse}.
	 */
	public void execute(final ServerFacade serverFacade) {
		GetPropertiesResponse response = serverFacade.getProperties(this);
		setResponse(response);
	}
	

	/**
	 * Downcasts.
	 */
	@Override
    public GetPropertiesResponse getResponse() {
        return (GetPropertiesResponse) super.getResponse();
    }

}

// Copyright (c) Naked Objects Group Ltd.
