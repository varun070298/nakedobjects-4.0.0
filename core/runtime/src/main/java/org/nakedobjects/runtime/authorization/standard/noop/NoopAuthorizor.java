package org.nakedobjects.runtime.authorization.standard.noop;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.runtime.authorization.standard.Authorizor;

public class NoopAuthorizor implements Authorizor {

	public void init() {
		// does nothing
	}
	
	public void shutdown() {
		// does nothing
	}
	
	public boolean isUsableInRole(String role, Identifier identifier) {
		return true;
	}

	public boolean isVisibleInRole(String user, Identifier identifier) {
		return true;
	}

}
