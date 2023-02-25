package org.nakedobjects.plugins.remoting.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.EncodingMarshaller;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoderDefault;
import org.nakedobjects.remoting.server.ServerConnection;
import org.nakedobjects.remoting.server.ServerConnectionDefault;
import org.nakedobjects.remoting.server.ServerConnectionHandler;
import org.nakedobjects.remoting.server.SocketsViewerAbstract;
import org.nakedobjects.remoting.transport.simple.SimpleTransport;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.webapp.NakedObjectsWebAppBootstrapper;

/**
 * Analogous to {@link SocketsViewerAbstract}; both ultimately delegate to
 * {@link ServerConnectionHandler}.
 */
public class EncodingOverHttpRemotingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private NakedObjectsSystem system;
	private NakedObjectConfiguration configuration;

	private ServerFacadeImpl serverFacade;

	@Override
	public void init() throws ServletException {
		super.init();
		system = NakedObjectsWebAppBootstrapper
				.getSystemBoundTo(getServletContext());
		configuration = system.getConfiguration();

		serverFacade = new ServerFacadeImpl(system.getSessionFactory()
				.getAuthenticationManager());
		serverFacade.setEncoder(ObjectEncoderDecoderDefault
				.create(configuration));
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletInputStream inputStream = request.getInputStream();
		ServletOutputStream outputStream = response.getOutputStream();

		ServerConnection serverConnection = createConnection(inputStream,
				outputStream);

		try {
			new ServerConnectionHandler(serverConnection).handleRequest();
		} catch (IOException ex) {
			// REVIEW: is this enough, or should we try to return a more
			// user-friendly exception or status?
			throw ex;
		} finally {
			// nothing to do
		}
	}

	private ServerConnection createConnection(ServletInputStream inputStream,
			ServletOutputStream outputStream) throws IOException {

		// TODO: should use installers to create these,
		// provides the opportunity to read in installer-specific config files.
		SimpleTransport transport = new SimpleTransport(configuration,
				inputStream, outputStream);
		EncodingMarshaller marshaller = new EncodingMarshaller(configuration,
				transport);

		// this is a no-op with the SimpleTransport, but include for consistency
		marshaller.connect();

		return new ServerConnectionDefault(serverFacade, marshaller);
	}
}
