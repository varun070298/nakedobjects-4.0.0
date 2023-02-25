package org.nakedobjects.remoting.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.EncodingMarshaller;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.remoting.transport.simple.SimpleTransport;


public class EncodingOverSocketsViewer extends SocketsViewerAbstract {
	
	public EncodingOverSocketsViewer(ObjectEncoderDecoder objectEncoderDecoder) {
		super(objectEncoderDecoder);
	}
	
    @Override
    protected ServerConnectionDefault createServerConnection(
            final InputStream input,
            final OutputStream output,
            final ServerFacade serverFacade) {
    	SimpleTransport transport = new SimpleTransport(getConfiguration(), input, output);
        EncodingMarshaller marshaller = new EncodingMarshaller(getConfiguration(), transport);
        try {
            marshaller.connect();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
        return new ServerConnectionDefault(serverFacade, marshaller);
    }
}

// Copyright (c) Naked Objects Group Ltd.
