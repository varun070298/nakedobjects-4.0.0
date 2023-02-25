package org.nakedobjects.remoting.protocol.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.protocol.IllegalRequestException;
import org.nakedobjects.remoting.protocol.MarshallerAbstract;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.remoting.transport.Transport;


public class SerializingMarshaller extends MarshallerAbstract {
    
	private static final Logger LOG = Logger.getLogger(SerializingMarshaller.class);
	
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public SerializingMarshaller(
    		final NakedObjectConfiguration configuration,
    		final Transport transport) {
    	super(configuration, transport);
    }

    ////////////////////////////////////////////////////////
    // connect (client-side impl)
    ////////////////////////////////////////////////////////

    @Override
    public void connect() throws IOException {
    	super.connect();
    	
    	this.input = new ObjectInputStream(getTransport().getInputStream());
        this.output = new ObjectOutputStream(getTransport().getOutputStream());
    }

    ////////////////////////////////////////////////////////
    // ServerMarshaller impl
    ////////////////////////////////////////////////////////

    public Object request(final Request request) throws IOException {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sending request" + request);
            }
            try {
                output.writeObject(request);
            } catch (final SocketException e) {
                reconnect();
                output.writeObject(request);
            }
            output.flush();
            final Object object = input.readObject();
            if (LOG.isDebugEnabled()) {
                LOG.debug("response received: " + object);
            }
            return object;
            /*
             * } catch (StreamCorruptedException e) { try { int available = input.available();
             * LOG.debug("error in reading; skipping bytes: " + available); input.skip(available); } catch
             * (IOException e1) { e1.printStackTrace(); } throw new ConnectionException(e.getMessage(), e);
             */
        } catch (final ClassNotFoundException e) {
            throw new ConnectionException("Failed request", e);
        }
    }


    ////////////////////////////////////////////////////////
    // ServerMarshaller impl
    ////////////////////////////////////////////////////////

    public Request readRequest() throws IOException {
        try {
            final Request request = (Request) input.readObject();
            return request;
        } catch (final ClassNotFoundException e) {
            throw new IllegalRequestException("unknown class received; closing connection: " + e.getMessage(), e);
        }
    }

    public void sendError(final NakedObjectException exception) throws IOException {
        send(exception);
    }

    public void sendResponse(final Object response) throws IOException {
        send(response);
    }

    private void send(final Object object) throws IOException {
        output.writeObject(object);
        output.flush();
    }

}
// Copyright (c) Naked Objects Group Ltd.
