package org.nakedobjects.plugins.remoting.http.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.nakedobjects.metamodel.commons.io.LazyInputStream;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.remoting.http.HttpRemotingConstants;
import org.nakedobjects.remoting.transport.TransportAbstract;

public class HttpClientTransport extends TransportAbstract {

	private HttpClient httpClient;
	private String url;

	private ByteArrayOutputStream outputStream;
	private InputStream inputStream;

	public HttpClientTransport(NakedObjectConfiguration configuration) {
		super(configuration);
	}

	// ///////////////////////////////////////////////////////////
	// init, shutdown
	// ///////////////////////////////////////////////////////////

	public void init() {
		httpClient = new HttpClient();
		url = getConfiguration().getString(
				HttpRemotingConstants.URL_KEY,
				HttpRemotingConstants.URL_DEFAULT);
	}

	public void shutdown() {
		httpClient = null;
	}

	// ///////////////////////////////////////////////////////////
	// connect, disconnect
	// ///////////////////////////////////////////////////////////

	public void connect() throws IOException {
		outputStream = new ByteArrayOutputStream();
	}

	public void disconnect() {
		inputStream = null;
		outputStream = null;
	}

	// ///////////////////////////////////////////////////////////
	// streams
	// ///////////////////////////////////////////////////////////

	/**
	 * Returns an {@link OutputStream} that writes into the request body of an
	 * HTTP POST, and will send on {@link OutputStream#flush() flush}.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * When first called, executes the HTTP POST, then returns the response
	 * body.
	 * 
	 * <p>
	 * Subsequent calls return the same input stream, at whatever position they
	 * have been processed.
	 */
	public InputStream getInputStream() throws IOException {
		if (inputStream == null) {
			inputStream = new LazyInputStream(
				new LazyInputStream.InputStreamProvider() {
					public InputStream getInputStream() throws IOException {
						PostMethod postMethod = new PostMethod(url);
						
						// copy over
						InputStreamRequestEntity requestEntity = new InputStreamRequestEntity(
								new ByteArrayInputStream(outputStream.toByteArray()));
						postMethod.setRequestEntity(requestEntity);
						
						// execute
						httpClient.executeMethod(postMethod);
						
						// clear for next time
						outputStream.reset();
						
						// return response
						return postMethod.getResponseBodyAsStream();
					}
				});
		}
		return inputStream;
	}

}
