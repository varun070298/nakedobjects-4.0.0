package org.nakedobjects.runtime.authentication.standard;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;

public abstract class PasswordRequestAuthenticatorAbstract extends AuthenticatorAbstract {
	
    public PasswordRequestAuthenticatorAbstract(final NakedObjectConfiguration configuration) {
    	super(configuration);
    }

    public final boolean canAuthenticate(final AuthenticationRequest request) {
        return request instanceof AuthenticationRequestPassword;
    }


}

// Copyright (c) Naked Objects Group Ltd.
