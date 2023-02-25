package org.nakedobjects.runtime.authentication.standard.fixture;

import java.util.List;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.runtime.authentication.AuthenticationRequestAbstract;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandard;




/**
 * For testing purposes, request corresponding to a {@link LogonFixture}.
 * 
 * <p>
 * Understood directly by {@link AuthenticationManagerStandard}.
 */
public class AuthenticationRequestLogonFixture extends AuthenticationRequestAbstract {
	
    public AuthenticationRequestLogonFixture(LogonFixture logonFixture) {
    	this(logonFixture.getUsername(), logonFixture.getRoles());
    }

    public AuthenticationRequestLogonFixture(final String name, final List<String> roles) {
    	super(name);
        setRoles(roles);
    }


}

// Copyright (c) Naked Objects Group Ltd.
