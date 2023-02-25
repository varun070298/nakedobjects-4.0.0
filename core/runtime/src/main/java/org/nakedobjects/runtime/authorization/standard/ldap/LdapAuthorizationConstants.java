package org.nakedobjects.runtime.authorization.standard.ldap;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.authentication.standard.ldap.LdapAuthenticationConstants;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.authorization.standard.AuthorizationConstants;

public final class LdapAuthorizationConstants {
	
	private static final String ROOT = 
		ConfigurationConstants.ROOT + AuthorizationManagerInstaller.TYPE + "." + LdapAuthorizationManagerInstaller.NAME + ".";
	
	public static final String SERVER_KEY = ROOT + "server";
	public static final String SERVER_DEFAULT = LdapAuthenticationConstants.SERVER_DEFAULT;
	
	public static final String LDAPDN_KEY = ROOT + "dn";
	
	public static final String APP_DN_KEY = ROOT + "application.dn";
	
	public static final String LEARN_KEY = AuthorizationConstants.LEARN;
	public static final boolean LEARN_DEFAULT = AuthorizationConstants.LEARN_DEFAULT;

	private LdapAuthorizationConstants() {}

}
