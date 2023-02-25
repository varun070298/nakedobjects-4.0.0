package org.nakedobjects.runtime.authentication.standard.exploration;

import java.util.List;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.runtime.authentication.AuthenticationRequestAbstract;




/**
 * For testing purposes, requests corresponding to an {@link ExplorationSession}.
 */
public class AuthenticationRequestExploration extends AuthenticationRequestAbstract {
	
	private static final String EXPLORATION_USER = "exploration";
	
	private final LogonFixture logonFixture;
	
    public AuthenticationRequestExploration() {
    	this(null);
    }
    
    public AuthenticationRequestExploration(LogonFixture logonFixture) {
    	super(logonFixture != null? logonFixture.getUsername(): EXPLORATION_USER);
    	this.logonFixture = logonFixture;
    }
    
    @Override
    public List<String> getRoles() {
    	return logonFixture!=null? logonFixture.getRoles(): super.getRoles();
    }

	public boolean isDefaultUser() {
		return EXPLORATION_USER.equals(getName());
	}

}

// Copyright (c) Naked Objects Group Ltd.
