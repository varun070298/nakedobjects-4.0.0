package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class ResolveObjectRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final IdentityData target;

    public ResolveObjectRequest(final AuthenticationSession session, final IdentityData target) {
        super(session);
        this.target = target;
        initialized();
    }

    public ResolveObjectRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.target = input.readEncodable(IdentityData.class);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeEncodable(target);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public IdentityData getTarget() {
		return target;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of an {@link ObjectData}.
	 */
    public void execute(final ServerFacade serverFacade) {
        ResolveObjectResponse response = serverFacade.resolveImmediately(this);
		setResponse(response);
    }

    /**
     * Downcasts
     */
    @Override
    public ResolveObjectResponse getResponse() {
        return (ResolveObjectResponse) super.getResponse();
    }


    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("target", target);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
