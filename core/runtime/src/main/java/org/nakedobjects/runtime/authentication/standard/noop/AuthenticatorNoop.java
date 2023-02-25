package org.nakedobjects.runtime.authentication.standard.noop;

import org.nakedobjects.metamodel.commons.component.Noop;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.standard.AuthenticatorAbstract;

public class AuthenticatorNoop extends AuthenticatorAbstract implements Noop {

    public AuthenticatorNoop(NakedObjectConfiguration configuration) {
		super(configuration);
	}

	public boolean canAuthenticate(AuthenticationRequest request) {
        return true;
    }

    public boolean isValid(AuthenticationRequest request) {
        return false;
    }

}


// Copyright (c) Naked Objects Group Ltd.
