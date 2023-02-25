package org.nakedobjects.runtime.authentication.standard;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestAbstract;
import org.nakedobjects.runtime.authentication.standard.exploration.AuthenticationRequestExploration;
import org.nakedobjects.runtime.authentication.standard.exploration.MultiUserExplorationSession;
import org.nakedobjects.runtime.system.DeploymentType;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(JMock.class)
public class AuthenticatorAbstractTest {
	
	private Mockery mockery = new JUnit4Mockery();

	private NakedObjectConfiguration mockConfiguration;
    private AuthenticatorAbstract authenticator;


    @Before
    public void setUp() {
    	mockConfiguration = mockery.mock(NakedObjectConfiguration.class);
    	 
    	authenticator = new AuthenticatorAbstract(mockConfiguration){
			public boolean canAuthenticate(AuthenticationRequest request) {
				return false;
			}
			public boolean isValid(AuthenticationRequest request) {
				return false;
			}};
    }

    @Test
    public void getConfiguration() throws Exception {
    	assertThat(authenticator.getConfiguration(), is(mockConfiguration));
    }


    @Test
    public void getDeploymentTypeForExploration() throws Exception {
    	final DeploymentType deploymentType = DeploymentType.EXPLORATION;
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(deploymentType.name()));
    	}});
    	assertThat(authenticator.getDeploymentType(), is(deploymentType));
    }

    @Test
    public void getDeploymentTypeForPrototype() throws Exception {
    	final DeploymentType deploymentType = DeploymentType.PROTOTYPE;
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(deploymentType.name()));
    	}});
    	assertThat(authenticator.getDeploymentType(), is(deploymentType));
    }

    @Test
    public void getDeploymentTypeForServer() throws Exception {
    	final DeploymentType deploymentType = DeploymentType.SERVER;
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(deploymentType.name()));
    	}});
    	assertThat(authenticator.getDeploymentType(), is(deploymentType));
    }

    @Test(expected=IllegalStateException.class)
    public void expectsThereToBeADeploymentTypeInNakedObjectsConfiguration() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue(null));
    	}});
    	authenticator.getDeploymentType();
    }

    @Test(expected=IllegalArgumentException.class)
    public void expectsThereToBeAValidDeploymentTypeInNakedObjectsConfiguration() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString("nakedobjects.deploymentType");
    		will(returnValue("GARBAGE"));
    	}});
    	authenticator.getDeploymentType();
    }

}

// Copyright (c) Naked Objects Group Ltd.
