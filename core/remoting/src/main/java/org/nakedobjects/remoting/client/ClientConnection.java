package org.nakedobjects.remoting.client;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.protocol.ClientMarshaller;
import org.nakedobjects.remoting.server.ServerConnection;

/**
 * Mediates between a running system (which has {@link Request}s that need servicing) and 
 * the {@link ClientMarshaller} that pushes the requests onto the network and pulls them
 * back. 
 * 
 * @see ServerConnection
 */
public interface ClientConnection extends ApplicationScopedComponent {

    ResponseEnvelope executeRemotely(Request request);

}

// Copyright (c) Naked Objects Group Ltd.
