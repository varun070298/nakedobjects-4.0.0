package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class GetObjectRequest extends RequestAbstract {
	
    private static final long serialVersionUID = 1L;
    private final Oid oid;
    private final String specificationName;

    public GetObjectRequest(final AuthenticationSession session, final Oid oid, final String specificationName) {
        super(session);
        this.oid = oid;
        this.specificationName = specificationName;
        initialized();
    }

    public GetObjectRequest(final DataInputExtended input) throws IOException {
        super(input);
        oid = input.readEncodable(Oid.class);
        specificationName = input.readUTF();
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeEncodable(oid);
        output.writeUTF(specificationName);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public Oid getOid() {
		return oid;
	}

	public String getSpecificationName() {
		return specificationName;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link GetObjectResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        GetObjectResponse response = serverFacade.getObject(this);
		setResponse(response);
    }

	/**
	 * Downcasts.
	 */
	@Override
    public GetObjectResponse getResponse() {
        return (GetObjectResponse) super.getResponse();
    }


    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("oid", oid);
        str.append("spec", specificationName);
        return str.toString();
    }
}

// Copyright (c) Naked Objects Group Ltd.
