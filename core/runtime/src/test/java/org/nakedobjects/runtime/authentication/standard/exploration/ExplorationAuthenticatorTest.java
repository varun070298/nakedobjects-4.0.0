package org.nakedobjects.runtime.authentication.standard.exploration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequestAbstract;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.SystemConstants;

@RunWith(JMock.class)
public class ExplorationAuthenticatorTest {
	
	private Mockery mockery = new JUnit4Mockery();

	private NakedObjectConfiguration mockConfiguration;
    private ExplorationAuthenticator authenticator;

	private AuthenticationRequestExploration explorationRequest;

	private SomeOtherAuthenticationRequest someOtherRequest;

    private static class SomeOtherAuthenticationRequest extends AuthenticationRequestAbstract {
		public SomeOtherAuthenticationRequest() {
			super("other");
		}
	}

    @Before
    public void setUp() {
    	mockConfiguration = mockery.mock(NakedObjectConfiguration.class);

    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue(DeploymentType.EXPLORATION.name()));
    	}});

    	explorationRequest = new AuthenticationRequestExploration();
    	someOtherRequest = new SomeOtherAuthenticationRequest();
    	 
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    }

    @Test
    public void canAuthenticateExplorationRequest() throws Exception {
    	assertThat(authenticator.canAuthenticate(explorationRequest), is(true));
    }

    @Test
    public void canAuthenticateSomeOtherTypeOfRequest() throws Exception {
		assertThat(authenticator.canAuthenticate(someOtherRequest), is(false));
    }

    @Test
    public void isValidExplorationRequestWhenRunningInExplorationMode() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
    		will(returnValue(DeploymentType.EXPLORATION.name()));
    	}});
		assertThat(authenticator.isValid(explorationRequest), is(true));
    }

    @Test
    public void isNotValidExplorationRequestWhenRunningInSomethingOtherThanExplorationMode() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
    		will(returnValue(DeploymentType.PROTOTYPE.name()));
    	}});
    	assertThat(authenticator.isValid(explorationRequest), is(false));
    }

    @Test(expected=IllegalStateException.class)
    public void expectsThereToBeADeploymentTypeInNakedObjectsConfiguration() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
    		will(returnValue(null));
    	}});
    	authenticator.isValid(explorationRequest);
    }

    @Test
    public void isValidSomeOtherTypeOfRequest() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
    		will(returnValue(DeploymentType.EXPLORATION.name()));
    	}});
    	assertThat(authenticator.canAuthenticate(someOtherRequest), is(false));
    }

}

// Copyright (c) Naked Objects Group Ltd.
