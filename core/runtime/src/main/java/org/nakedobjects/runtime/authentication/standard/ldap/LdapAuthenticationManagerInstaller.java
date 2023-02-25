package org.nakedobjects.runtime.authentication.standard.ldap;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.standard.Authenticator;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandardInstallerAbstract;


public class LdapAuthenticationManagerInstaller extends AuthenticationManagerStandardInstallerAbstract {
	
	public static String NAME = "ldap";

    public LdapAuthenticationManagerInstaller() {
        super(NAME);
    }

    @Override
    protected Authenticator createAuthenticator(NakedObjectConfiguration configuration) {
        return new LdapAuthenticator(configuration);
    }
}
