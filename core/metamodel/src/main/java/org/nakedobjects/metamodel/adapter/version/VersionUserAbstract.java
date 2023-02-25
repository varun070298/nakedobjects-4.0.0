package org.nakedobjects.metamodel.adapter.version;

import java.io.IOException;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;



public abstract class VersionUserAbstract implements Version, Encodable {
    private final String user;

    public VersionUserAbstract(final String user) {
        this.user = user;
        initialized();
    }

    public VersionUserAbstract(DataInputExtended input) throws IOException {
    	this.user = input.readUTF();
    	initialized();
	}

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(user);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////


    public String getUser() {
        return user;
    }

}
// Copyright (c) Naked Objects Group Ltd.
