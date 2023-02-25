package org.nakedobjects.plugins.remoting.http;

import org.nakedobjects.metamodel.config.ConfigurationConstants;

public class HttpRemotingConstants {
	
	private static final String ROOT = ConfigurationConstants.ROOT + "transport.http.";
	
	/**
	 * Key used to lookup URL to which the <tt>EncodingOverHttpRemotingServlet</tt> is mapped.
	 */
	public static final String URL_KEY = ROOT + "url";
	/**
	 * Default value for {@link #URL_KEY}.
	 */
	public static final String URL_DEFAULT = "http://localhost:8080/remoting.svc";

	private HttpRemotingConstants(){}

}
