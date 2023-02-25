package org.nakedobjects.plugins.headless.junit.internal;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.standard.Authenticator;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandardInstallerAbstract;
import org.nakedobjects.runtime.authentication.standard.noop.AuthenticatorNoop;

public class NoopAuthenticationManagerInstaller extends AuthenticationManagerStandardInstallerAbstract {

	public NoopAuthenticationManagerInstaller() {
		super("noop");
	}

	@Override
	protected Authenticator createAuthenticator(
			NakedObjectConfiguration configuration) {
		return new AuthenticatorNoop(configuration);
	}
}