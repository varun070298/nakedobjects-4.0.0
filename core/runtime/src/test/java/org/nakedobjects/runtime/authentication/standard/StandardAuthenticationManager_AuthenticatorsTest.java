package org.nakedobjects.runtime.authentication.standard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.NoAuthenticatorException;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.authentication.standard.noop.AuthenticatorNoop;

@RunWith(JMock.class)
public class StandardAuthenticationManager_AuthenticatorsTest {

	private Mockery mockery = new JUnit4Mockery();

	private NakedObjectConfiguration mockConfiguration;
	private AuthenticationManagerStandard authenticationManager;
    
    @Before
    public void setUp() throws Exception {
        mockConfiguration = mockery.mock(NakedObjectConfiguration.class);

    	authenticationManager = new AuthenticationManagerStandard(mockConfiguration);
    }

    @Test
    public void shouldInitiallyHaveNoAuthenticators() throws Exception {
        assertThat(authenticationManager.getAuthenticators().size(), is(0));
    }

    @Test(expected=NoAuthenticatorException.class)
    public void shouldNotBeAbleToAuthenticateWithNoAuthenticators() throws Exception {
        authenticationManager.authenticate(new AuthenticationRequestPassword("foo", "bar"));
    }

    @Test
    public void shouldBeAbleToAddAuthenticators() throws Exception {
        Authenticator authenticator = new AuthenticatorNoop(mockConfiguration);
        authenticationManager.addAuthenticator(authenticator);
        assertThat(authenticationManager.getAuthenticators().size(), is(1));
        assertThat(authenticationManager.getAuthenticators().get(0), is(sameInstance(authenticator)));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void shouldNotBeAbleToModifyReturnedAuthenticators() throws Exception {
        List<Authenticator> authenticators = authenticationManager.getAuthenticators();
        authenticators.add(new AuthenticatorNoop(mockConfiguration));
    }

}


// Copyright (c) Naked Objects Group Ltd.
