package org.nakedobjects.runtime.authentication.standard;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.standard.exploration.AuthenticationRequestExploration;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationAuthenticator;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationAuthenticatorConstants;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationSession;
import org.nakedobjects.runtime.authentication.standard.exploration.MultiUserExplorationSession;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.SystemConstants;


@RunWith(JMock.class)
public class MultiUserExplorationSessionTest {

	private Mockery mockery = new JUnit4Mockery();
	
    private MultiUserExplorationSession session;
	private NakedObjectConfiguration mockConfiguration;
	private ExplorationAuthenticator authenticator;

    @Before
    public void setUp() {
    	mockConfiguration = mockery.mock(NakedObjectConfiguration.class);
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(SystemConstants.DEPLOYMENT_TYPE_KEY);
    		will(returnValue(DeploymentType.EXPLORATION.name()));
    	}});
    }

    @Test
    public void testNameDefaultsToFirstUser() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	AuthenticationSession session = authenticator.authenticate(new AuthenticationRequestExploration(), "");
    	
    	Assert.assertEquals("fred", session.getUserName());
    }

    @Test
    public void testValidateCode() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	AuthenticationSession session = authenticator.authenticate(new AuthenticationRequestExploration(), "xxx");

    	Assert.assertEquals("xxx", session.getValidationCode());
    }

    @Test
    public void testNoRolesSpecifiedForFirstUser() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	AuthenticationSession session = authenticator.authenticate(new AuthenticationRequestExploration(), "");
    	
        Assert.assertEquals(0, session.getRoles().size());
    }

    @Test
    public void testForMultipleUser() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	AuthenticationSession authSession = authenticator.authenticate(new AuthenticationRequestExploration(), "");
    	
    	assertThat(authSession, is(MultiUserExplorationSession.class));

        assertThat(authSession.getUserName(), is(equalTo("fred")));
    }

    @Test
    public void testForSingleUser() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("sven"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	AuthenticationSession authSession = authenticator.authenticate(new AuthenticationRequestExploration(), "");
    	assertThat(authSession, is(SimpleSession.class));

        assertThat(authSession.getUserName(), is(equalTo("sven")));
    }

    @Test
    public void testNoUsersSpecified() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue(null));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);

    	AuthenticationSession authSession = authenticator.authenticate(new AuthenticationRequestExploration(), "");
    	assertThat(authSession, is(ExplorationSession.class));
    }

    @Test
    public void testOtherUsers() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	this.session = (MultiUserExplorationSession) authenticator.authenticate(new AuthenticationRequestExploration(), "");

    	Set<String> availableSessions = session.getUserNames();
        Assert.assertEquals(4, availableSessions.size());
        Assert.assertTrue(availableSessions.contains("fred"));
        Assert.assertTrue(availableSessions.contains("sven"));
        Assert.assertTrue(availableSessions.contains("bob"));
        Assert.assertTrue(availableSessions.contains("dick"));
    }
    
    @Test
    public void testChangeUser() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	this.session = (MultiUserExplorationSession) authenticator.authenticate(new AuthenticationRequestExploration(), "");

    	session.setCurrentSession("bob");
        Assert.assertEquals("bob", session.getUserName());
    }
    
    @Test
    public void testRolesExist() throws Exception {
    	mockery.checking(new Expectations(){{
    		allowing(mockConfiguration).getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
    		will(returnValue("fred, sven:admin|sales|marketing, bob:sales, dick"));
    	}});
    	authenticator = new ExplorationAuthenticator(mockConfiguration);
    	this.session = (MultiUserExplorationSession) authenticator.authenticate(new AuthenticationRequestExploration(), "");

    	session.setCurrentSession("sven");
        List<String> roles = session.getRoles();
        Assert.assertEquals(3, roles.size());
        Assert.assertEquals("admin", roles.get(0));
        Assert.assertEquals("sales", roles.get(1));
        Assert.assertEquals("marketing", roles.get(2));
    }
}

// Copyright (c) Naked Objects Group Ltd.
