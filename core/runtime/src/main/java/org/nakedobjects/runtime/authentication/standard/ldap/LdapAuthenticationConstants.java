package org.nakedobjects.runtime.authentication.standard.ldap;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;

public final class LdapAuthenticationConstants {
	
	public static final String ROOT = ConfigurationConstants.ROOT + AuthenticationManagerInstaller.TYPE + "." + LdapAuthenticationManagerInstaller.NAME + ".";
	
	public static final String SERVER_KEY = ROOT + "server";
	public static final String SERVER_DEFAULT = "com.sun.jndi.ldap.LdapCtxFactory";
	
	public static final String LDAPDN_KEY = ROOT + "dn";
	
	public static String FILTER = "(objectclass=organizationalRole)";

	private LdapAuthenticationConstants(){}

}
