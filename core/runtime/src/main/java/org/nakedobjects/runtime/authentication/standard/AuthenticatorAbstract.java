package org.nakedobjects.runtime.authentication.standard;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.SystemConstants;



public abstract class AuthenticatorAbstract implements Authenticator {

	private final NakedObjectConfiguration configuration;
	
	////////////////////////////////////////////////////////
	// constructor
	////////////////////////////////////////////////////////

    public AuthenticatorAbstract(NakedObjectConfiguration configuration) {
    	this.configuration = configuration;
    }


	////////////////////////////////////////////////////////
	// init, shutdown
	////////////////////////////////////////////////////////
    
	public void init() {
		// does nothing.
	}

	public void shutdown() {
		// does nothing.
	}


	////////////////////////////////////////////////////////
	// API
	////////////////////////////////////////////////////////

	/**
	 * Default implementation returns a {@link SimpleSession};
	 * can be overridden if required.
	 */
    public AuthenticationSession authenticate(AuthenticationRequest request, String code) {
    	if (!isValid(request)) {
    		return null;
    	}
    	return new SimpleSession(request.getName(), request.getRoles(), code);
    }

	////////////////////////////////////////////////////////
	// Helpers
	////////////////////////////////////////////////////////

    /**
     * Helper method for convenience of implementations that depend on the {@link DeploymentType}.
     */
	protected DeploymentType getDeploymentType() {
		String deploymentTypeStr = getConfiguration().getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
		if(deploymentTypeStr==null) {
			throw new IllegalStateException("Expect value for '" + SystemConstants.DEPLOYMENT_TYPE_KEY + "' to be bound into NakedObjectsConfiguration");
		}
    	return DeploymentType.lookup(deploymentTypeStr);
	}


	////////////////////////////////////////////////////////
	// Injected (via constructor)
	////////////////////////////////////////////////////////

	public NakedObjectConfiguration getConfiguration() {
		return configuration;
	}
	
}

// Copyright (c) Naked Objects Group Ltd.
