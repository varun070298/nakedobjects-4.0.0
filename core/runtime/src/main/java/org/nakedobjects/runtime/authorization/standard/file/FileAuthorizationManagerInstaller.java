package org.nakedobjects.runtime.authorization.standard.file;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.standard.AuthorizationManagerStandardInstallerAbstract;
import org.nakedobjects.runtime.authorization.standard.Authorizor;


public class FileAuthorizationManagerInstaller extends AuthorizationManagerStandardInstallerAbstract {

    public static final String NAME = "file";

	public FileAuthorizationManagerInstaller() {
        super(NAME);
    }

    @Override
    protected Authorizor createAuthorizor(NakedObjectConfiguration configuration) {
        return new FileAuthorizor(configuration);
    }

}
