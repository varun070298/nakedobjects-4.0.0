package org.nakedobjects.remoting.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;

public interface Transport extends ApplicationScopedComponent {

	void connect() throws IOException;

	void disconnect();

	InputStream getInputStream() throws IOException;
	
	OutputStream getOutputStream() throws IOException;

}
