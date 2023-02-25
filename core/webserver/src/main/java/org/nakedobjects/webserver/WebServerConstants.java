package org.nakedobjects.webserver;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.web.EmbeddedWebServerInstaller;


public final class WebServerConstants {
	
    private static final String ROOT = ConfigurationConstants.ROOT + EmbeddedWebServerInstaller.TYPE + ".";
    
	public static final String EMBEDDED_WEB_SERVER_PORT_KEY = ROOT + "port";
    public static final int EMBEDDED_WEB_SERVER_PORT_DEFAULT = 8080;

	public static final String EMBEDDED_WEB_SERVER_ADDRESS_KEY = ROOT + "address";
    public static final String EMBEDDED_WEB_SERVER_ADDRESS_DEFAULT = "localhost";

	public static final String EMBEDDED_WEB_SERVER_RESOURCE_BASE_KEY = ROOT + "webapp";
    public static final String EMBEDDED_WEB_SERVER_RESOURCE_BASE_DEFAULT = ""; // or "webapp" ??

    private WebServerConstants() {}

}
