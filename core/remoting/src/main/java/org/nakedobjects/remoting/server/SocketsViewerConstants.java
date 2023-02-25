package org.nakedobjects.remoting.server;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.remoting.transport.socket.SocketTransportConstants;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;


public final class SocketsViewerConstants {

	private static final String ROOT = 
		ConfigurationConstants.ROOT + NakedObjectsViewerInstaller.TYPE + ".";
	
	/**
	 * Used by all the XxxOverSockets viewers.  
	 * 
	 * <p>
	 * Equivalent to {@link SocketTransportConstants#PORT_KEY}.
	 */
	public static final String SERVER_PORT = ROOT + "sockets.port";
	
	private SocketsViewerConstants() {}

}
