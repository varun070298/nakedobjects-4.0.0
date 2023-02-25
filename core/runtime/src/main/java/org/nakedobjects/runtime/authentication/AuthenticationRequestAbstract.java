package org.nakedobjects.runtime.authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;





public abstract class AuthenticationRequestAbstract implements AuthenticationRequest {
	
    private final String name;
    private final List<String> roles = new ArrayList<String>();

    public AuthenticationRequestAbstract(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public void setRoles(final List<String> roles) {
    	this.roles.clear();
        this.roles.addAll(roles);
    }

}

// Copyright (c) Naked Objects Group Ltd.
