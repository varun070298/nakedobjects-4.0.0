package org.nakedobjects.runtime.authentication.standard.fixture;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.standard.AuthenticatorAbstract;
import org.nakedobjects.runtime.system.DeploymentType;

public class LogonFixtureAuthenticator extends AuthenticatorAbstract {
	
    public LogonFixtureAuthenticator(final NakedObjectConfiguration configuration) {
    	super(configuration);
    }

    /**
     * Can authenticate if a {@link AuthenticationRequestLogonFixture}.
     */
    public final boolean canAuthenticate(final AuthenticationRequest request) {
        return request instanceof AuthenticationRequestLogonFixture;
    }

    /**
     * Valid providing running in {@link DeploymentType#isExploring() exploration} or
     * {@link DeploymentType#isPrototyping() prototyping} mode.
     */
    public final boolean isValid(final AuthenticationRequest request) {
    	DeploymentType deploymentType = getDeploymentType();
		return deploymentType.isExploring() || deploymentType.isPrototyping();
    }

}

// Copyright (c) Naked Objects Group Ltd.
