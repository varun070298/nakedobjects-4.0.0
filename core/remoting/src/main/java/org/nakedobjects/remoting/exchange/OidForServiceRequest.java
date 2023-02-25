package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class OidForServiceRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final String serviceId;

    /**
     * provided for serialization only!
     */
    public OidForServiceRequest() {
        this(null, null);
    }

    public OidForServiceRequest(final AuthenticationSession session, final String id) {
    	super(session);
    	this.serviceId = id;
    	initialized();
    }
    
    public OidForServiceRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.serviceId = input.readUTF();
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeUTF(serviceId);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public String getServiceId() {
		return serviceId;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a simple {@link Boolean}.
	 */
    public void execute(final ServerFacade serverFacade) {
        setResponse(serverFacade.oidForService(this));
    }

    /**
     * Downcasts.
     */
    @Override
    public OidForServiceResponse getResponse() {
        return (OidForServiceResponse) super.getResponse();
    }

    
    /////////////////////////////////////////////////////////
    // tostring
    /////////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("sequence", getId());
        str.append("id", serviceId);
        return str.toString();
    }

}

// Copyright (c) Naked Objects Group Ltd.
