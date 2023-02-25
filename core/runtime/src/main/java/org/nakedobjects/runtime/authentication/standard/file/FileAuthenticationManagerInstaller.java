package org.nakedobjects.runtime.authentication.standard.file;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.standard.Authenticator;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandardInstallerAbstract;


public class FileAuthenticationManagerInstaller extends AuthenticationManagerStandardInstallerAbstract {

    public static final String NAME = "file";

	public FileAuthenticationManagerInstaller() {
        super(NAME);
    }

    @Override
    protected Authenticator createAuthenticator(final NakedObjectConfiguration configuration) {
        return new FileAuthenticator(configuration);
    }

}
