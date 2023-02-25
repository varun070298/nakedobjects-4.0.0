package org.nakedobjects.runtime.authorization.standard;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;


public abstract class AuthorizorAbstract implements Authorizor {
	
	private final NakedObjectConfiguration configuration;
	
	public AuthorizorAbstract(final NakedObjectConfiguration configuration) {
		this.configuration = configuration;
	}

	public NakedObjectConfiguration getConfiguration() {
		return configuration;
	}
	

}
