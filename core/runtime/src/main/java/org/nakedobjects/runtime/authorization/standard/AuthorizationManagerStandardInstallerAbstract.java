package org.nakedobjects.runtime.authorization.standard;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.installers.InstallerAbstract;


public abstract class AuthorizationManagerStandardInstallerAbstract extends InstallerAbstract implements AuthorizationManagerInstaller {

    public AuthorizationManagerStandardInstallerAbstract(String name) {
    	super(AuthorizationManagerInstaller.TYPE, name);
    }
    
	public AuthorizationManager createAuthorizationManager() {
        final AuthorizationManagerStandard authorizationManager = 
        	new AuthorizationManagerStandard(getConfiguration());
        final Authorizor authorizor = createAuthorizor(getConfiguration());
        authorizationManager.setAuthorizor(authorizor);
        return authorizationManager;
    }

    /**
     * Hook method
     * @return
     */
    protected abstract Authorizor createAuthorizor(final NakedObjectConfiguration configuration);


}
