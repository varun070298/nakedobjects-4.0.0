package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class FindInstancesRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final PersistenceQueryData criteria;

    public FindInstancesRequest(final AuthenticationSession session, final PersistenceQueryData criteria) {
        super(session);
        this.criteria = criteria;
        initialized();
    }

    public FindInstancesRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.criteria = input.readEncodable(PersistenceQueryData.class);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeEncodable(criteria);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public PersistenceQueryData getCriteria() {
		return criteria;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link FindInstancesResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        FindInstancesResponse response = serverFacade.findInstances(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public FindInstancesResponse getResponse() {
        return (FindInstancesResponse) super.getResponse();
    }

    
    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("criteria", criteria);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
