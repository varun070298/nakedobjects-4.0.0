package org.nakedobjects.remoting.client.authentication;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.exchange.CloseSessionRequest;
import org.nakedobjects.remoting.exchange.CloseSessionResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandard;


public class AuthenticationManagerProxy extends AuthenticationManagerStandard {

    private ServerFacade serverFacade;
	@SuppressWarnings("unused")
	private ObjectEncoderDecoder encoderDecoder;

    public AuthenticationManagerProxy(
    		final NakedObjectConfiguration configuration, 
    		final ServerFacade serverFacade, 
    		final ObjectEncoderDecoder encoderDecoder) {
    	super(configuration);
    	this.serverFacade = serverFacade;
    	this.encoderDecoder = encoderDecoder;
    	
        ProxyAuthenticator authenticator = 
        	new ProxyAuthenticator(getConfiguration(), serverFacade, encoderDecoder);
		addAuthenticator(authenticator);
    }


    @Override
    public void closeSession(final AuthenticationSession authSession) {
        @SuppressWarnings("unused")
		CloseSessionResponse response = serverFacade.closeSession(new CloseSessionRequest(authSession));
        super.closeSession(authSession);
    }

}

// Copyright (c) Naked Objects Group Ltd.
