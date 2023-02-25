package org.nakedobjects.remoting.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.nakedobjects.remoting.NakedObjectsRemoteException;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.protocol.ClientMarshaller;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.runtime.persistence.ConcurrencyException;



/**
 * Default implementation of {@link ClientConnection} that delegates to
 * {@link ClientMarshaller} supplied in {@link ClientConnectionDefault constructor}.
 */
public class ClientConnectionDefault  implements ClientConnection {

    @SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ClientConnectionDefault.class);

    private final ClientMarshaller marshaller;


    //////////////////////////////////////////////////////////
    // Constructor
    //////////////////////////////////////////////////////////

    public ClientConnectionDefault(
    		final ClientMarshaller marshaller) {
        this.marshaller = marshaller;
    }

    protected ClientMarshaller getMarshaller() {
        return marshaller;
    }

    
    //////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////

    public void init() {
    	marshaller.init();
    }

    public void shutdown() {
    	marshaller.shutdown();
    }


    //////////////////////////////////////////////////////////
    // reconnect, connect, disconnect
    //////////////////////////////////////////////////////////

    private void connect() {
        try {
			marshaller.connect();
        } catch (final IOException e) {
            throw new ConnectionException("Connection failure", e);
		}
    }

    private void disconnect() {
    	marshaller.disconnect();
    }


    //////////////////////////////////////////////////////////
    // executeRemotely
    //////////////////////////////////////////////////////////

    public ResponseEnvelope executeRemotely(final Request request) {
    	connect();
        try {
            return executeRemotelyElseException(request);
        } catch (final IOException e) {
            throw new ConnectionException("Failed request", e);
        } finally {
        	disconnect();
        }
    }

	private ResponseEnvelope executeRemotelyElseException(final Request request)
			throws IOException {

		Object response = marshaller.request(request);
		
		if (response instanceof ConcurrencyException) {
			throw (ConcurrencyException) response;
		} else if (response instanceof Exception) {
			throw new NakedObjectsRemoteException(
					"Exception occurred on server", (Throwable) response);
		} else {
			return (ResponseEnvelope) response;
		}
	}


}
// Copyright (c) Naked Objects Group Ltd.
