package org.nakedobjects.plugins.headless.embedded;

import java.util.List;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.plugins.headless.embedded.internal.PersistenceState;

public class EmbeddedContextNoop implements EmbeddedContext {
	
	public AuthenticationSession getAuthenticationSession() {
		return null;
	}

	public PersistenceState getPersistenceState(Object object) {
		return null;
	}

	public Object instantiate(Class<?> type) {
		return null;
	}
	public void makePersistent(Object object) {
	}
	public void remove(Object object) {
	}


	public void resolve(Object parent) {
	}
	public void resolve(Object parent, Object field) {
	}
	public void objectChanged(Object object) {
	}

	public <T> List<T> allMatchingQuery(Query<T> query) {
		return null;
	}
	public <T> T firstMatchingQuery(Query<T> query) {
		return null;
	}

	
	public void commit() {
	}
	public boolean flush() {
		return false;
	}
	
	public void informUser(String message) {
	}
	public void warnUser(String message) {
	}
	public void raiseError(String message) {
	}

}
