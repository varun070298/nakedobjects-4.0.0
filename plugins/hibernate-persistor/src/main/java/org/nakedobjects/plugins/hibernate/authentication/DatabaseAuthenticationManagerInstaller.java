package org.nakedobjects.plugins.hibernate.authentication;


import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.standard.Authenticator;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandardInstallerAbstract;


public class DatabaseAuthenticationManagerInstaller extends AuthenticationManagerStandardInstallerAbstract {


    public DatabaseAuthenticationManagerInstaller() {
        super("database");
    }

    @Override
    protected Authenticator createAuthenticator(NakedObjectConfiguration configuration) {
        return new DatabaseAuthenticator(configuration);
    }

}
