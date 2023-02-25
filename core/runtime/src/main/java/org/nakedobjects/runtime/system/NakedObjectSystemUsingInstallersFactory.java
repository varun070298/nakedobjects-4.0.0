package org.nakedobjects.runtime.system;

import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.system.installers.NakedObjectsSystemUsingInstallers;


/**
 * Implementation of {@link NakedObjectSystemFactory} that uses {@link InstallerLookup} to convert the names
 * of components into actual component instances.
 */
public class NakedObjectSystemUsingInstallersFactory implements NakedObjectSystemFactory {

    private final InstallerLookup installerLookup;

    // //////////////////////////////////////////////////////////
    // constructor
    // //////////////////////////////////////////////////////////

    public NakedObjectSystemUsingInstallersFactory(final InstallerLookup installerLookup) {
        this.installerLookup = installerLookup;
    }

    // //////////////////////////////////////////////////////////
    // init, shutdown
    // //////////////////////////////////////////////////////////

    public void init() {
    // nothing to do
    }

    public void shutdown() {
    // nothing to do
    }

    // //////////////////////////////////////////////////////////
    // main API
    // //////////////////////////////////////////////////////////

    public NakedObjectsSystem createSystem(final DeploymentType deploymentType) {

        final NakedObjectsSystemUsingInstallers system = new NakedObjectsSystemUsingInstallers(deploymentType, installerLookup);

        system.lookupAndSetAuthenticatorAndAuthorization(deploymentType);
        system.lookupAndSetUserProfileFactoryInstaller();
        system.lookupAndSetFixturesInstaller();
        return system;
    }

    // //////////////////////////////////////////////////////////
    // Dependencies (injected or defaulted in constructor)
    // //////////////////////////////////////////////////////////

    public InstallerLookup getInstallerLookup() {
        return installerLookup;
    }

}
