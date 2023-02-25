package org.nakedobjects.runtime.authorization;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;


public abstract class AuthorizationManagerAbstract implements AuthorizationManager {

	private final NakedObjectConfiguration configuration;

    ///////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////
    
	public AuthorizationManagerAbstract(final NakedObjectConfiguration configuration) {
    	this.configuration = configuration;
    }

	public NakedObjectConfiguration getConfiguration() {
		return configuration;
	}

}
