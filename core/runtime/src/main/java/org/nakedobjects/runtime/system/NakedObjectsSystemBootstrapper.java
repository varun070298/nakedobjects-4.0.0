package org.nakedobjects.runtime.system;

import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.installers.InstallerLookup;
import org.nakedobjects.runtime.installers.InstallerLookupDefault;


/**
 * Creates and initializes {@link NakedObjectsSystem} using the provided {@link ConfigurationBuilder} or
 * {@link InstallerLookup}.
 */
public final class NakedObjectsSystemBootstrapper {

    private final InstallerLookup installerLookup;

    public static InstallerLookup createAndInitializeInstallerLookup(
            final ConfigurationBuilder configurationBuilder,
            Class<?> loadedBy) {
        InstallerLookup installerLookup = new InstallerLookupDefault(loadedBy);
        configurationBuilder.injectInto(installerLookup);
        installerLookup.init();
        return installerLookup;
    }

    public NakedObjectsSystemBootstrapper(final ConfigurationBuilder configurationBuilder, Class<?> loadedBy) {
        this(createAndInitializeInstallerLookup(configurationBuilder, loadedBy));
    }

    public NakedObjectsSystemBootstrapper(final InstallerLookup installerLookup) {
        this.installerLookup = installerLookup;
    }

    public NakedObjectsSystem bootSystem(final DeploymentType deploymentType) {
    	// make deploymentType available in the configuration
        ConfigurationBuilder configurationBuilder = installerLookup.getConfigurationBuilder();
		configurationBuilder.add(SystemConstants.DEPLOYMENT_TYPE_KEY, deploymentType.name());
		
        NakedObjectSystemUsingInstallersFactory systemFactory = new NakedObjectSystemUsingInstallersFactory(installerLookup);
        systemFactory.init();

        final NakedObjectsSystem system = systemFactory.createSystem(deploymentType);
        system.init();

        return system;
    }

}
// Copyright (c) Naked Objects Group Ltd.
