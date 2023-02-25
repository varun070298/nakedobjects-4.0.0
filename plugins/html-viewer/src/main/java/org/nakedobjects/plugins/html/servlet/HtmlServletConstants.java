package org.nakedobjects.plugins.html.servlet;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.ConfigurationConstants;

public final class HtmlServletConstants {
	
	static final String PROPERTY_BASE = ConfigurationConstants.ROOT + "viewer.html.";
	
	static final String DEBUG_KEY = PROPERTY_BASE + "debug";
	
	static final String ENCODING_KEY = PROPERTY_BASE + "encoding";
	static final String ENCODING_DEFAULT = "ISO-8859-1";

	/**
	 * Binding to the {@link AuthenticationSession}.
	 */
	static final String AUTHENTICATION_SESSION_CONTEXT_KEY = "nof-context";

	public static final String LOGON_APP_PAGE = "logon.app";
	
	private HtmlServletConstants() {}

}
