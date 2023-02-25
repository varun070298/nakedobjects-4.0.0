package org.nakedobjects.remoting.client.authorization;

import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.exchange.AuthorizationRequestUsability;
import org.nakedobjects.remoting.exchange.AuthorizationRequestVisibility;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authorization.AuthorizationManagerAbstract;


public class AuthorizationManagerProxy extends AuthorizationManagerAbstract {

    private final Map<String,Boolean> visibilityCache = new HashMap<String,Boolean>();
    private final Map<String,Boolean> usabilityCache = new HashMap<String,Boolean>();
    
    private final ServerFacade serverFacade;
	private final ObjectEncoderDecoder encoderDecoder;

    
    //////////////////////////////////////////////////////////////////
    // Constructor
    //////////////////////////////////////////////////////////////////
    
    public AuthorizationManagerProxy(NakedObjectConfiguration configuration, final ServerFacade serverFacade, final ObjectEncoderDecoder encoderDecoder) {
    	super(configuration);
        this.serverFacade = serverFacade;
        this.encoderDecoder = encoderDecoder;
    }

    //////////////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////////////

    public void init() {}

    public void shutdown() {}

    //////////////////////////////////////////////////////////////////
    // API
    //////////////////////////////////////////////////////////////////

    public boolean isUsable(final AuthenticationSession session, NakedObject target, final Identifier identifier) {
		final IdentityData targetData = encoderDecoder.encodeIdentityData(target);

        final String idString = identifier.toIdentityString(Identifier.CLASS_MEMBERNAME_PARMS);
		if (!usabilityCache.containsKey(idString)) {
			AuthorizationResponse response = serverFacade.authorizeUsability(new AuthorizationRequestUsability(session, targetData, idString));
			final Boolean authorized = isAuthorized(response);
		    usabilityCache.put(idString, authorized);
		}
		return usabilityCache.get(idString);
    }

    public boolean isVisible(final AuthenticationSession session, NakedObject target, final Identifier identifier) {
		final IdentityData targetData = encoderDecoder.encodeIdentityData(target);

		final String idString = identifier.toIdentityString(Identifier.CLASS_MEMBERNAME_PARMS);
		if (!visibilityCache.containsKey(idString)) {
		    AuthorizationRequestVisibility request = new AuthorizationRequestVisibility(session, targetData, idString);
			final AuthorizationResponse response = serverFacade.authorizeVisibility(request);
			Boolean authorized = isAuthorized(response);
			visibilityCache.put(idString, authorized);
		}
		return visibilityCache.get(idString);
    }

    private Boolean isAuthorized(final AuthorizationResponse response) {
    	// defensive coding is all (shouldn't really be null)
    	return response != null? response.isAuthorized(): false;
    }
}

