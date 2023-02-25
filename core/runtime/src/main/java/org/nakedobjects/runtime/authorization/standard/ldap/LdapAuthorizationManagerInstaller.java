package org.nakedobjects.runtime.authorization.standard.ldap;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.standard.AuthorizationManagerStandardInstallerAbstract;
import org.nakedobjects.runtime.authorization.standard.Authorizor;

public class LdapAuthorizationManagerInstaller extends
		AuthorizationManagerStandardInstallerAbstract {

	public static String NAME = "ldap";

	public LdapAuthorizationManagerInstaller() {
		super(NAME);
	}

	@Override
	protected Authorizor createAuthorizor(NakedObjectConfiguration configuration) {
		return new LdapAuthorizor(configuration);
	}

}
