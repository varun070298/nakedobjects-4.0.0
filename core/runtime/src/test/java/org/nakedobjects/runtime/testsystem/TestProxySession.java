package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.metamodel.authentication.AuthenticationSessionAbstract;


public class TestProxySession extends AuthenticationSessionAbstract {
	
	private static final long serialVersionUID = 1L;
	
    public TestProxySession() {
		super("unit tester", null);
	}
    
}

// Copyright (c) Naked Objects Group Ltd.
