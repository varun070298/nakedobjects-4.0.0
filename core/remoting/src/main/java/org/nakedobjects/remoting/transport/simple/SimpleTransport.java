package org.nakedobjects.remoting.transport.simple;

import java.io.InputStream;
import java.io.OutputStream;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.transport.TransportAbstract;

/**
 * Simple implementation that simply wraps an already existing
 * {@link InputStream} and {@link OutputStream}.
 * 
 * <p>
 * Originally written to assist with refactoring.
 */
public class SimpleTransport extends TransportAbstract {
	
	private final InputStream inputStream;
	private final OutputStream outputStream;
	
	public SimpleTransport(NakedObjectConfiguration configuration,
			InputStream inputStream, OutputStream outputStream) {
		super(configuration);
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}


	//////////////////////////////////////////////////////
	// init, shutdown
	//////////////////////////////////////////////////////
	
	public void init() {
		// does nothing
	}

	public void shutdown() {
		// does nothing
	}

	//////////////////////////////////////////////////////
	// connect, disconnect
	//////////////////////////////////////////////////////

	public void connect() {
		// does nothing
	}

	public void disconnect() {
		// does nothing
	}

	//////////////////////////////////////////////////////
	// input & output streams
	//////////////////////////////////////////////////////

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}
