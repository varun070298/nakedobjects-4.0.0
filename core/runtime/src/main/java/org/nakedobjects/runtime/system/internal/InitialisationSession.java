package org.nakedobjects.runtime.system.internal;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSessionAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;

public final class InitialisationSession extends AuthenticationSessionAbstract {
	
	private static final long serialVersionUID = 1L;

	public InitialisationSession() {
		super("initialisation", "");
	}

	public InitialisationSession(DataInputExtended input) throws IOException {
		super(input);
	}
	
}