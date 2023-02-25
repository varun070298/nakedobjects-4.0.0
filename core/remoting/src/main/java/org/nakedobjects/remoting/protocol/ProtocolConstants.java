package org.nakedobjects.remoting.protocol;

import org.nakedobjects.metamodel.config.ConfigurationConstants;


public final class ProtocolConstants {

	private static final String ROOT = 
		ConfigurationConstants.ROOT + ProtocolInstaller.TYPE + ".";
	
	public static final String KEEPALIVE_KEY = ROOT + "keepalive";
	public static final boolean KEEPALIVE_DEFAULT = false;
	
	public static final String DEBUGGING_KEY = ROOT + "debugging";
	public static final boolean DEBUGGING_DEFAULT = false;

	
	private ProtocolConstants() {}

}
