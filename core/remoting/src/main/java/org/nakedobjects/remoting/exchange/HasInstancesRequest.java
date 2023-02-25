package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.facade.ServerFacade;

public class HasInstancesRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final String specificationName;

    public HasInstancesRequest(final AuthenticationSession session, final String specificationName) {
        super(session);
        this.specificationName = specificationName;
        initialized();
    }

    public HasInstancesRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.specificationName = input.readUTF();
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeUTF(specificationName);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public String getSpecificationName() {
		return specificationName;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link HasInstancesResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        final HasInstancesResponse response = serverFacade.hasInstances(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public HasInstancesResponse getResponse() {
        return (HasInstancesResponse) super.getResponse();
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("class", specificationName);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
