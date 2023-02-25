package org.nakedobjects.remoting.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.remoting.protocol.serialize.SerializingMarshaller;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.remoting.transport.simple.SimpleTransport;


public class SerializingOverSocketsViewer extends SocketsViewerAbstract {
	
	
    public SerializingOverSocketsViewer(
			ObjectEncoderDecoder objectEncoderDecoder) {
		super(objectEncoderDecoder);
	}

	@Override
    protected ServerConnection createServerConnection(
            final InputStream input,
            final OutputStream output,
            final ServerFacade serverFacade) {
    	SimpleTransport transport = new SimpleTransport(getConfiguration(), input, output);
        SerializingMarshaller serverMarshaller = new SerializingMarshaller(getConfiguration(), transport);
        try {
			serverMarshaller.connect();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
        return new ServerConnectionDefault(serverFacade, serverMarshaller);
    }
}

// Copyright (c) Naked Objects Group Ltd.
