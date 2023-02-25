package org.nakedobjects.plugins.headless.embedded;

import java.util.List;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.plugins.headless.embedded.internal.PersistenceState;

public interface EmbeddedContext {

	AuthenticationSession getAuthenticationSession();
	
	Object instantiate(Class<?> type);
	
	void resolve(Object parent);
	void resolve(Object parent, Object field);
	void objectChanged(Object object);

	void makePersistent(Object object);
	void remove(Object object);

	PersistenceState getPersistenceState(Object object);
	
	<T> List<T> allMatchingQuery(Query<T> query);
	<T> T firstMatchingQuery(Query<T> query);

	boolean flush();
	void commit();

	void informUser(String message);
	void warnUser(String message);
	void raiseError(String message);





}
