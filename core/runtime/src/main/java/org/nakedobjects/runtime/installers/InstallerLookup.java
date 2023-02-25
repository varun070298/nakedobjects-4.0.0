package org.nakedobjects.runtime.installers;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderAware;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorInstaller;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstaller;
import org.nakedobjects.runtime.imageloader.TemplateImageLoaderInstaller;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;
import org.nakedobjects.runtime.persistence.services.ServicesInstaller;
import org.nakedobjects.runtime.remoting.ClientConnectionInstaller;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.NakedObjectsSystem;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;
import org.nakedobjects.runtime.web.EmbeddedWebServerInstaller;


/**
 * The installers correspond more-or-less to the configurable top-level components of
 * {@link NakedObjectsSystem}.
 * 
 * <p>
 * The methods of {@link InstallerRepository} may be called without {@link #init() initializing} this class,
 * but other methods may not.
 */
public interface InstallerLookup extends InstallerRepository, ApplicationScopedComponent, ConfigurationBuilderAware, Injectable {

    // /////////////////////////////////////////////////////////
    // metamodel
    // /////////////////////////////////////////////////////////

    NakedObjectReflectorInstaller reflectorInstaller(final String requested);

    // /////////////////////////////////////////////////////////
    // framework
    // /////////////////////////////////////////////////////////

    AuthenticationManagerInstaller authenticationManagerInstaller(String requested, boolean isExploring);

    AuthorizationManagerInstaller authorizationManagerInstaller(String requested, boolean isExploring);

    FixturesInstaller fixturesInstaller(String requested);

    ServicesInstaller servicesInstaller(final String requested);

    TemplateImageLoaderInstaller templateImageLoaderInstaller(String requested);

    PersistenceMechanismInstaller persistenceMechanismInstaller(final String requested, final DeploymentType deploymentType);

    UserProfileStoreInstaller userProfilePersistenceMechanismInstaller(final String requested, DeploymentType deploymentType);

    NakedObjectsViewerInstaller viewerInstaller(final String requested, final String defaultName);

    NakedObjectsViewerInstaller viewerInstaller(final String requested);

    /**
     * Client-side of <tt>remoting</tt>, specifying how to access the server.
     * 
     * <p>
     * Note that this lookup is called in three different contexts:
     * <ul>
     * <li>the <tt>NakedObjectsExecutionContextFactoryUsingInstallers</tt> uses this to lookup the
     * {@link PersistenceMechanismInstaller} (may be a <tt>ProxyPersistor</tt>)</li>
     * <li>the <tt>NakedObjectsExecutionContextFactoryUsingInstallers</tt> also uses this to lookup the
     * {@link FacetDecoratorInstaller}; adds in remoting facets.</li>
     * <li>the <tt>NakedObjectsSystemUsingInstallers</tt> uses this to lookup the
     * {@link AuthenticationManagerInstaller}.</li>
     * </ul>
     */
    ClientConnectionInstaller clientConnectionInstaller(final String requested);

    EmbeddedWebServerInstaller embeddedWebServerInstaller(final String requested);

    // /////////////////////////////////////////////////////////
    // framework - generic
    // /////////////////////////////////////////////////////////

    <T extends Installer> T getInstaller(final Class<T> cls, final String requested);

    <T extends Installer> T getInstaller(final Class<T> cls);

    <T extends Installer> T getInstaller(final String implClassName);

    // /////////////////////////////////////////////////////////
    // configuration
    // /////////////////////////////////////////////////////////

    /**
     * Injects self into candidate
     */
    <T> T injectDependenciesInto(T candidate);

    /**
     * Returns a <i>snapshot</i> of the current {@link NakedObjectConfiguration}.
     * 
     * <p>
     * The {@link NakedObjectConfiguration} could subsequently be appended to if further {@link Installer}s
     * are loaded.
     */
    NakedObjectConfiguration getConfiguration();

    // /////////////////////////////////////////////////////////
    // dependencies (injected)
    // /////////////////////////////////////////////////////////

    /**
     * Injected.
     */
    ConfigurationBuilder getConfigurationBuilder();

}

// Copyright (c) Naked Objects Group Ltd.
