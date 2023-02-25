package org.nakedobjects.runtime.authentication.standard;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;


public abstract class AuthenticationManagerStandardInstallerAbstract extends InstallerAbstract implements
        AuthenticationManagerInstaller {

    public AuthenticationManagerStandardInstallerAbstract(String name) {
        super(AuthenticationManagerInstaller.TYPE, name);
    }

    public final AuthenticationManager createAuthenticationManager() {
        final AuthenticationManagerStandard authenticationManager = new AuthenticationManagerStandard(getConfiguration());
        final Authenticator authenticator = createAuthenticator(getConfiguration());
        authenticationManager.addAuthenticator(authenticator);
        return authenticationManager;
    }

    /**
     * Hook method
     * 
     * @return
     */
    protected abstract Authenticator createAuthenticator(final NakedObjectConfiguration configuration);

}
