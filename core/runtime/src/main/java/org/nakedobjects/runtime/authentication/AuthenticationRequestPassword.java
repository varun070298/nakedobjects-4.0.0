package org.nakedobjects.runtime.authentication;




public class AuthenticationRequestPassword extends AuthenticationRequestAbstract {
	
    private final String password;

    public AuthenticationRequestPassword(final String name, final String password) {
    	super(name);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}

// Copyright (c) Naked Objects Group Ltd.
