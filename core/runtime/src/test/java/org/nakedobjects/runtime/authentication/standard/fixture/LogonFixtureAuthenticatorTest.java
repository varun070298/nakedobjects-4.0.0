package org.nakedobjects.runtime.authentication.standard.fixture;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.fixtures.LogonFixture;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequestAbstract;
import org.nakedobjects.runtime.authentication.standard.exploration.MultiUserExplorationSession;
import org.nakedobjects.runtime.system.DeploymentType;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(JMock.class)
public class LogonFixtureAuthenticatorTest {
	
	private Mockery mockery = new JUnit4Mockery();

	private NakedObjectConfiguration mockConfiguration;
    private LogonFixtureAuthenticator authenticator;

	private AuthenticationRequestLogonFixture logonFixtureRequest;

	private SomeOtherAuthenticationRequest someOtherRequest;

    private static class SomeOtherAuthenticationRequest extends AuthenticationRequestAbstract {
		public SomeOtherAuthenticationRequest() {
			super("other");
		}
	}

    @Before
    public void setUp() {
    	mockConfiguration = mockery.mock(NakedObjectConfiguration.class);
    	 
    	logonFixtureRequest = new AuthenticationRequestLogonFixture(new LogonFixture("joebloggs"));
    	someOtherRequest = new SomeOtherAuthenticationRequest();
    	authenticator = new LogonFixtureAuthenticator(mockConfiguration);
    }

    @Test
    public void canAuthenticateExplorationRequest() throws Exception {
		assertThat(authenticator.canAuthenticate(logonFixtureRequest), is(true));
    }

    @Test
    public void canAuthenticateSomeOtherTypeOfRequest() throws Exception {
		assertThat(authenticator.canAuthenticate(someOtherRequest), is(false));
    }

    @Test
    public void isValidLogonFixtureRequestWhenRunningInExplorationMode() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(DeploymentType.EXPLORATION.name()));
    	}});
    	assertThat(authenticator.isValid(logonFixtureRequest), is(true));
    }

    @Test
    public void isValidLogonFixtureRequestWhenRunningInPrototypeMode() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(DeploymentType.PROTOTYPE.name()));
    	}});
    	assertThat(authenticator.isValid(logonFixtureRequest), is(true));
    }

    @Test
    public void isNotValidExplorationRequestWhenRunningInSomethingOtherThanExplorationOrPrototypeMode() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(DeploymentType.SERVER.name()));
    	}});
    	assertThat(authenticator.isValid(logonFixtureRequest), is(false));
    }

    @Test(expected=IllegalStateException.class)
    public void expectsThereToBeADeploymentTypeInNakedObjectsConfiguration() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(null));
    	}});
    	authenticator.isValid(logonFixtureRequest);
    }

    @Test
    public void isValidSomeOtherTypeOfRequest() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(DeploymentType.EXPLORATION.name()));
    	}});
    	assertThat(authenticator.canAuthenticate(new SomeOtherAuthenticationRequest()), is(false));
    }

}

// Copyright (c) Naked Objects Group Ltd.
