package org.nakedobjects.runtime.fixturesinstaller;

import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.applib.switchuser.SwitchUserService;
import org.nakedobjects.applib.switchuser.SwitchUserServiceAware;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.standard.fixture.AuthenticationRequestLogonFixture;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;


public class SwitchUserServiceImpl implements SwitchUserService {

    public SwitchUserServiceImpl() {
    }

    public void switchUser(final String username, final String... roles) {
    	getTransactionManager().endTransaction();
    	NakedObjectsContext.closeSession();
    	LogonFixture logonFixture = new LogonFixture(username, roles);
    	AuthenticationRequestLogonFixture authRequest = new AuthenticationRequestLogonFixture(logonFixture);
    	AuthenticationSession session = getAuthenticationManager().authenticate(authRequest);
        NakedObjectsContext.openSession(session);
        getTransactionManager().startTransaction();
    }

	public void injectInto(Object fixture) {
    	if (fixture instanceof SwitchUserServiceAware) {
    		SwitchUserServiceAware serviceAware = (SwitchUserServiceAware) fixture;
    		serviceAware.setService(this);
    	}
	}

	
	private static AuthenticationManager getAuthenticationManager() {
		return NakedObjectsContext.getAuthenticationManager();
	}
	
	private static NakedObjectTransactionManager getTransactionManager() {
		return NakedObjectsContext.getTransactionManager();
	}
	
}

// Copyright (c) Naked Objects Group Ltd.
