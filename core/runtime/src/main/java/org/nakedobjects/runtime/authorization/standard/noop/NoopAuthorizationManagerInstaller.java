package org.nakedobjects.runtime.authorization.standard.noop;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.standard.AuthorizationManagerStandardInstallerAbstract;
import org.nakedobjects.runtime.authorization.standard.Authorizor;

public class NoopAuthorizationManagerInstaller extends
		AuthorizationManagerStandardInstallerAbstract {

	public NoopAuthorizationManagerInstaller() {
		super("noop");
	}
 
	@Override
	protected Authorizor createAuthorizor(NakedObjectConfiguration configuration) {
		return new NoopAuthorizor();
	}

}
