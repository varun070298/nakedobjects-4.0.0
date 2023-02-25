package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.authorization.AuthorizationManager;

public class AuthorizationManagerNoop implements AuthorizationManager {

	public void init() {
	}
	
	public void shutdown() {
	}
	
	public boolean isUsable(AuthenticationSession session, NakedObject target, Identifier identifier) {
		return true;
	}

	public boolean isVisible(AuthenticationSession session,
			NakedObject target, Identifier identifier) {
		return true;
	}

}
