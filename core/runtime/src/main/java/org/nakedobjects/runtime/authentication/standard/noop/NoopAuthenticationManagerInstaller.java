package org.nakedobjects.runtime.authentication.standard.noop;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.standard.Authenticator;
import org.nakedobjects.runtime.authentication.standard.AuthenticationManagerStandardInstallerAbstract;


public class NoopAuthenticationManagerInstaller extends AuthenticationManagerStandardInstallerAbstract {

    public NoopAuthenticationManagerInstaller() {
        super("noop");
    }

    @Override
    protected Authenticator createAuthenticator(final NakedObjectConfiguration configuration) {
        return new AuthenticatorNoop(configuration);
    }

}
