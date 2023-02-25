package org.nakedobjects.runtime.transaction;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public abstract class PersistenceCommandAbstract implements PersistenceCommand {
	
	private final NakedObject adapter;

	public PersistenceCommandAbstract(NakedObject adapter) {
		super();
		this.adapter = adapter;
	}

	public NakedObject onObject() {
		return adapter;
	}

	///////////////////////////////////////////////////////////////////////
	// Dependencies (from context)
	///////////////////////////////////////////////////////////////////////
	
	protected static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}


}
