package org.nakedobjects.runtime.authentication.standard.singleuser;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSessionAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public final class SingleUserSession extends AuthenticationSessionAbstract implements Encodable {
	
	private static final long serialVersionUID = 1L;
	
    private static final String DEFAULT_USER_NAME = "exploration";

    /**
     * Defaults validation code to <tt>""</tt>.
     */
    public SingleUserSession() {
    	this("");
    }

    public SingleUserSession(String code) {
        super(DEFAULT_USER_NAME, code);
        initialized();
    }

	public SingleUserSession(DataInputExtended input) throws IOException {
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

}
// Copyright (c) Naked Objects Group Ltd.
