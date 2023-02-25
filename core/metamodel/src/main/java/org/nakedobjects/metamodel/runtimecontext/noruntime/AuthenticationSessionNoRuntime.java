package org.nakedobjects.metamodel.runtimecontext.noruntime;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSessionAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;

public class AuthenticationSessionNoRuntime extends AuthenticationSessionAbstract {

	private static final long serialVersionUID = 1L;

	public AuthenticationSessionNoRuntime() {
		super("metamodel", "dummy");
	}

	public AuthenticationSessionNoRuntime(DataInputExtended input) throws IOException {
		super(input);
	}

}
