package org.nakedobjects.remoting.client.authentication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.ensure.Ensure;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.OpenSessionResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.authentication.standard.AuthenticatorAbstract;
import org.nakedobjects.runtime.authentication.standard.PasswordRequestAuthenticatorAbstract;
import org.nakedobjects.runtime.authentication.standard.SimpleSession;

public final class ProxyAuthenticator extends PasswordRequestAuthenticatorAbstract {
	
	private final ServerFacade serverFacade;
	private final ObjectEncoderDecoder encoderDecoder;
	
	public ProxyAuthenticator(
			NakedObjectConfiguration configuration, 
			ServerFacade serverFacade, ObjectEncoderDecoder encoderDecoder) {
		super(configuration);
		Ensure.ensureThatArg(serverFacade, is(not(nullValue())));
		Ensure.ensureThatArg(encoderDecoder, is(not(nullValue())));
		this.serverFacade = serverFacade;
		this.encoderDecoder = encoderDecoder;
	}

	/**
	 * Whereas the default implementation of {@link AuthenticatorAbstract#authenticate(AuthenticationRequest, String)}
	 * delegates to this method, this implementation has it the other way around.
	 */
	public boolean isValid(AuthenticationRequest request) {
		// our implementation does not use the code, so can pass in null.
		return authenticate(request, null) != null;
	}
	
	/**
	 * Delegates to the provided {@link ServerFacade} to {@link ServerFacade#authenticate(String)}.
	 *
	 * <p>
	 * Compare to the {@link AuthenticatorAbstract#authenticate(AuthenticationRequest, String) default implementation}
	 * which calls {@link #isValid(AuthenticationRequest)} and then returns
	 * a {@link SimpleSession}.  
	 */
	@Override
	public AuthenticationSession authenticate(AuthenticationRequest authRequest,
			String code) {
        final AuthenticationRequestPassword passwordRequest = (AuthenticationRequestPassword) authRequest;
        final String username = passwordRequest.getName();
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        final String password = passwordRequest.getPassword();
        
		OpenSessionRequest request = new OpenSessionRequest(username, password);
		OpenSessionResponse response = serverFacade.openSession(request);
		return response.getSession();
	}
}