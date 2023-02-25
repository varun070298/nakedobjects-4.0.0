package org.nakedobjects.plugins.hibernate.authorization;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.standard.AuthorizationManagerStandardInstallerAbstract;
import org.nakedobjects.runtime.authorization.standard.Authorizor;

public class DatabaseAuthorizationManagerInstaller extends
		AuthorizationManagerStandardInstallerAbstract {


	public DatabaseAuthorizationManagerInstaller() {
		super("database");
	}

	@Override
	protected Authorizor createAuthorizor(NakedObjectConfiguration configuration) {
		return new DatabaseAuthorizor(configuration);
	}

}
