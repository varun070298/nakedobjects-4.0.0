package org.nakedobjects.runtime.authentication.standard.singleuser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.runtime.authentication.AuthenticationRequestAbstract;


/**
 * Requests corresponding to an {@link SingleUserSession}.
 */
public class AuthenticationRequestSingleUser extends AuthenticationRequestAbstract {
	
	private static final String SINGLE_USER_NAME = "self";
	private static final String SINGLE_USER_ROlE_NAME = "default_role";
	private final List<String> roles;
	
    public AuthenticationRequestSingleUser() {
    	super(SINGLE_USER_NAME);
    	roles = Collections.unmodifiableList(Arrays.asList(SINGLE_USER_ROlE_NAME));
    }
    
    @Override
    public List<String> getRoles() {
    	return roles;
    }

}

// Copyright (c) Naked Objects Group Ltd.
