package org.nakedobjects.runtime.authentication;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;

public interface AuthenticationManager extends ApplicationScopedComponent {

    /**
     * Caches and returns an authentication {@link AuthenticationSession} if the {@link AuthenticationRequest request} is 
     * valid; otherwise returns <tt>null</tt>.
     */
    AuthenticationSession authenticate(AuthenticationRequest request);

    /**
     * Whether the provided {@link AuthenticationSession} is still valid.
     */
    boolean isSessionValid(AuthenticationSession authenticationSession);
    
    void closeSession(AuthenticationSession authenticationSession);
    
}

// Copyright (c) Naked Objects Group Ltd.
