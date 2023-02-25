package org.nakedobjects.runtime.authentication.standard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;

@RunWith(JMock.class)
public class StandardAuthenticationManager_AuthenticationTest {

	private Mockery mockery = new JUnit4Mockery();
	
    private AuthenticationManagerStandard authenticationManager;

    private NakedObjectConfiguration mockConfiguration;
    private RandomCodeGenerator mockRandomCodeGenerator;
    private Authenticator mockAuthenticator;
    private AuthenticationSession mockAuthSession;

    
    @Before
    public void setUp() throws Exception {
    	mockConfiguration = mockery.mock(NakedObjectConfiguration.class);
    	mockRandomCodeGenerator = mockery.mock(RandomCodeGenerator.class);
    	mockAuthenticator = mockery.mock(Authenticator.class);
    	mockAuthSession = mockery.mock(AuthenticationSession.class);
    	
        authenticationManager = new AuthenticationManagerStandard(mockConfiguration);
        authenticationManager.addAuthenticator(mockAuthenticator);
		authenticationManager.setRandomCodeGenerator(mockRandomCodeGenerator);

    	mockery.checking(new Expectations(){{
    		allowing(mockAuthenticator).canAuthenticate(with(any(AuthenticationRequest.class)));
    		will(returnValue(true));

    		allowing(mockAuthenticator).authenticate(
    				with(any(AuthenticationRequest.class)), 
    				with(any(String.class)));
    		will(returnValue(mockAuthSession));

    		allowing(mockRandomCodeGenerator).generateRandomCode();
    		will(returnValue("123456"));
    		
    		allowing(mockAuthSession).getValidationCode();
    		will(returnValue("123456"));

    		allowing(mockAuthSession).hasUserNameOf("foo");
    		will(returnValue(true));

    		allowing(mockAuthSession).getUserName();
    		will(returnValue("foo"));

    	}});
    }


    @Test
    public void newlyCreatedAuthenticationSessionShouldBeValid() throws Exception {
    	AuthenticationRequestPassword request = new AuthenticationRequestPassword("foo", "bar");
		AuthenticationSession session = authenticationManager.authenticate(request);
		
		assertThat(authenticationManager.isSessionValid(session), is(true));
    }


}


// Copyright (c) Naked Objects Group Ltd.
