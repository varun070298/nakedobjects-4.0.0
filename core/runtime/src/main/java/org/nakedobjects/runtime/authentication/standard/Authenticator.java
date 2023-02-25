package org.nakedobjects.runtime.authentication.standard;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;

public interface Authenticator extends ApplicationScopedComponent {
	
	boolean canAuthenticate(AuthenticationRequest request);
	
    boolean isValid(AuthenticationRequest request);

    /**
     * @param code  - a hint; is guaranteed to be unique, but the authenticator decides whether to use it or not.
     */
	AuthenticationSession authenticate(AuthenticationRequest request, String code);


}

// Copyright (c) Naked Objects Group Ltd.
