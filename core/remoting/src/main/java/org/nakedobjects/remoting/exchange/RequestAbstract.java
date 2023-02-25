package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;


public abstract class RequestAbstract implements Request {
	
	
	private static int nextId = 0;
    protected transient Object response;
    private final int id;
    protected final AuthenticationSession session;

    public RequestAbstract(final AuthenticationSession session) {
        this.session = session;
        this.id = nextId++;
        initialized();
    }

    public RequestAbstract(final DataInputExtended input) throws IOException {
        id = input.readInt();
        session = input.readEncodable(AuthenticationSession.class);
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeInt(id);
        output.writeEncodable(session);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////


    public final void setResponse(final Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public AuthenticationSession getSession() {
        return session;
    }

    public int getId() {
        return id;
    }
}
// Copyright (c) Naked Objects Group Ltd.
