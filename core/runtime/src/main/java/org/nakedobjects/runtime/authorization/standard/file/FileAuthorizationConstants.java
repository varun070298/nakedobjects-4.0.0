package org.nakedobjects.runtime.authorization.standard.file;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.authorization.standard.AuthorizationConstants;

public final class FileAuthorizationConstants {
	
	private static final String ROOT = 
		ConfigurationConstants.ROOT + AuthorizationManagerInstaller.TYPE + "." + FileAuthorizationManagerInstaller.NAME + ".";
	
	public static final String WHITELIST_RESOURCE_KEY = ROOT + "whitelist";
	public static final String WHITELIST_RESOURCE_DEFAULT = "allow";
	
	public static final String BLACKLIST_RESOURCE = ROOT + "blacklist";
	public static final String BLACKLIST_RESOURCE_DEFAULT = "";
	
	public static final String LEARN = AuthorizationConstants.LEARN;
	public static final boolean LEARN_DEFAULT = AuthorizationConstants.LEARN_DEFAULT;

	private FileAuthorizationConstants(){}

}
