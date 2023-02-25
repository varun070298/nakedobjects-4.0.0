package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.Noop;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;

public class AuthenticationManagerNoop implements AuthenticationManager, Noop {

    public void init() {}
    
    public void shutdown() {}
    
    public AuthenticationSession authenticate(AuthenticationRequest request) {
        return null;
    }

    public void closeSession(AuthenticationSession session) {}

    public boolean isSessionValid(AuthenticationSession session) {
        return false;
    }

    public void testSetSession(AuthenticationSession authenticationSession) {}

}


// Copyright (c) Naked Objects Group Ltd.
