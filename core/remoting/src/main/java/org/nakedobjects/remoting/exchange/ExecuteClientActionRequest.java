package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class ExecuteClientActionRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final int[] types;
    private final ReferenceData[] data;

    public ExecuteClientActionRequest(final AuthenticationSession session, final ReferenceData[] data, final int[] types) {
        super(session);
        this.data = data;
        this.types = types;
        initialized();
    }

    public ExecuteClientActionRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.data = input.readEncodables(ReferenceData.class);
        this.types = input.readInts();
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
    	output.writeEncodables(data);
    	output.writeInts(types);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////
	
	public ReferenceData[] getData() {
		return data;
	}
	public int[] getTypes() {
		return types;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////
	
	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link ExecuteClientActionResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        ExecuteClientActionResponse response = serverFacade.executeClientAction(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public ExecuteClientActionResponse getResponse() {
        return (ExecuteClientActionResponse) super.getResponse();
    }

    
    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("data", data.length);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
