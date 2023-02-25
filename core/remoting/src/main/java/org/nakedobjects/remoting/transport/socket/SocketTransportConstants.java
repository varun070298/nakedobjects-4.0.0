package org.nakedobjects.remoting.transport.socket;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.remoting.transport.TransportInstaller;


public final class SocketTransportConstants {
    
	public static final String ROOT = 
		ConfigurationConstants.ROOT + TransportInstaller.TYPE + "." + "sockets.";

	public static final String PORT_KEY = ROOT + "port";
	public static final int PORT_DEFAULT = 9580;

	public static final String HOST_KEY = ROOT + "host";
	public static final String HOST_DEFAULT = "localhost";

	public static final String PROFILING_KEY = ROOT + "profiling";
	public static final boolean PROFILING_DEFAULT = true;

	
    private SocketTransportConstants() {}

}
