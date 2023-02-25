package org.nakedobjects.runtime.installers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.metamodel.commons.about.ComponentDetails;
import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.ensure.Ensure;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.factory.InstanceCreationClassException;
import org.nakedobjects.metamodel.commons.factory.InstanceCreationException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.commons.factory.UnavailableClassException;
import org.nakedobjects.metamodel.commons.lang.CastUtils;
import org.nakedobjects.metamodel.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NotFoundPolicy;
import org.nakedobjects.metamodel.specloader.FacetDecoratorInstaller;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorInstaller;
import org.nakedobjects.runtime.authentication.AuthenticationManagerInstaller;
import org.nakedobjects.runtime.authentication.standard.noop.NoopAuthenticationManagerInstaller;
import org.nakedobjects.runtime.authorization.AuthorizationManagerInstaller;
import org.nakedobjects.runtime.authorization.standard.noop.NoopAuthorizationManagerInstaller;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstaller;
import org.nakedobjects.runtime.imageloader.TemplateImageLoaderInstaller;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;
import org.nakedobjects.runtime.persistence.services.ServicesInstaller;
import org.nakedobjects.runtime.remoting.ClientConnectionInstaller;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;
import org.nakedobjects.runtime.web.EmbeddedWebServerInstaller;


/**
 * This class retrieves named {@link Installer}s from those loaded at creation, updating the
 * {@link NakedObjectConfiguration} as it goes.
 * 
 * <p>
 * A list of possible classes are read in from the resource file <tt>installer-registry.properties</tt>. Each
 * installer has a unique name (with respect to its type) that will be compared when one of this classes
 * methods are called. These are instantiated when requested.
 * 
 * <p>
 * Note that it <i>is</i> possible to use an {@link Installer} implementation even if it has not been
 * registered in <tt>installer-registry.properties</tt> : just specify the {@link Installer}'s fully qualified
 * class name.
 */
public class InstallerLookupDefault implements InstallerLookup {

    private static final Logger LOG = Logger.getLogger(InstallerLookupDefault.class);

    public final String INSTALLER_REGISTRY_FILE = "installer-registry.properties";

    private final List<Installer> installerList = new ArrayList<Installer>();
    private final Class<?> cls;

    /**
     * A mutable representation of the {@link NakedObjectConfiguration configuration}, injected prior to
     * {@link #init()}.
     * 
     * <p>
     * 
     * @see #setConfigurationBuilder(ConfigurationBuilder)
     */
    private ConfigurationBuilder configurationBuilder;

    // ////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////

    public InstallerLookupDefault(Class<?> cls) {
        this.cls = cls;
        loadInstallers();
    }

    private void loadInstallers() {
        final InputStream in = getInstallerRegistryStream(INSTALLER_REGISTRY_FILE, cls);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                final String className = StringUtils.firstWord(line);
                if (className.length() == 0 || className.startsWith("#")) {
                    continue;
                }
                try {
                    final Installer object = (Installer) InstanceFactory.createInstance(className);
                    LOG.debug("created component installer: " + object.getName() + " - " + className);
                    installerList.add(object);
                } catch (final UnavailableClassException e) {
                    LOG.info("component installer not found; it will not be available: " + className);
                } catch (final InstanceCreationClassException e) {
                    LOG.info("instance creation exception: " + e.getMessage());
                } catch (final InstanceCreationException e) {
                    throw e;
                }
            }
        } catch (final IOException e) {
            throw new NakedObjectException(e);
        } finally {
            close(reader);
        }

        List<ComponentDetails> installerVersionList = new ArrayList<ComponentDetails>();
        for (Installer installer : installerList) {
            installerVersionList.add(new InstallerVersion(installer));
        }
        AboutNakedObjects.setComponentDetails(installerVersionList);
    }

    // ////////////////////////////////////////////////////////
    // InstallerRepository impl.
    // ////////////////////////////////////////////////////////

    /**
     * This method (and only this method) may be called prior to {@link #init() initialization}.
     */
    public Installer[] getInstallers(final Class<?> cls) {
        final List<Installer> list = new ArrayList<Installer>();
        for (final Installer comp : installerList) {
            if (cls.isAssignableFrom(comp.getClass())) {
                list.add(comp);
            }
        }
        return (Installer[]) list.toArray(new Installer[list.size()]);
    }

    // ////////////////////////////////////////////////////////
    // init, shutdown
    // ////////////////////////////////////////////////////////

    public void init() {
        ensureDependenciesInjected();
    }

    private void ensureDependenciesInjected() {
        Ensure.ensureThatState(configurationBuilder, is(not(nullValue())));
    }

    public void shutdown() {
    // nothing to do.
    }

    // ////////////////////////////////////////////////////////
    // Type-safe Lookups
    // ////////////////////////////////////////////////////////

    public AuthenticationManagerInstaller authenticationManagerInstaller(String requested, boolean useNoOp) {
        return getInstaller(AuthenticationManagerInstaller.class, requested, SystemConstants.AUTHENTICATION_INSTALLER_KEY, 
               useNoOp ? NoopAuthenticationManagerInstaller.class.getName() : SystemConstants.AUTHENTICATION_DEFAULT );
    }

    public AuthorizationManagerInstaller authorizationManagerInstaller(String requested, boolean useNoOp) {
        return getInstaller(AuthorizationManagerInstaller.class, requested, SystemConstants.AUTHORIZATION_INSTALLER_KEY,
                useNoOp ? NoopAuthorizationManagerInstaller.class.getName() : SystemConstants.AUTHORIZATION_DEFAULT);
    }

    public FixturesInstaller fixturesInstaller(String requested) {
        return getInstaller(FixturesInstaller.class, requested, SystemConstants.FIXTURES_INSTALLER_KEY,
                SystemConstants.FIXTURES_INSTALLER_DEFAULT);
    }

    public TemplateImageLoaderInstaller templateImageLoaderInstaller(String requested) {
        return getInstaller(TemplateImageLoaderInstaller.class, requested, SystemConstants.IMAGE_LOADER_KEY,
                SystemConstants.IMAGE_LOADER_DEFAULT);
    }

    public PersistenceMechanismInstaller persistenceMechanismInstaller(final String requested, final DeploymentType deploymentType) {
        String persistorDefault = deploymentType.isExploring() || deploymentType.isPrototyping() ? SystemConstants.OBJECT_PERSISTOR_NON_PRODUCTION_DEFAULT
                : SystemConstants.OBJECT_PERSISTOR_PRODUCTION_DEFAULT;
        return getInstaller(PersistenceMechanismInstaller.class, requested, SystemConstants.OBJECT_PERSISTOR_KEY,
                persistorDefault);
    }

    public UserProfileStoreInstaller userProfilePersistenceMechanismInstaller(String requested, DeploymentType deploymentType) {
        String profileStoreDefault = deploymentType.isExploring() || deploymentType.isPrototyping() ? SystemConstants.USER_PROFILE_STORE_NON_PRODUCTION_DEFAULT
                : SystemConstants.USER_PROFILE_STORE_PRODUCTION_DEFAULT;
        return getInstaller(UserProfileStoreInstaller.class, requested, SystemConstants.USER_PROFILE_STORE_KEY,
                profileStoreDefault);
    }

    public NakedObjectReflectorInstaller reflectorInstaller(final String requested) {
        return getInstaller(NakedObjectReflectorInstaller.class, requested, SystemConstants.REFLECTOR_KEY,
                SystemConstants.REFLECTOR_DEFAULT);
    }

    public EmbeddedWebServerInstaller embeddedWebServerInstaller(final String requested) {
        return getInstaller(EmbeddedWebServerInstaller.class, requested, SystemConstants.WEBSERVER_KEY,
                SystemConstants.WEBSERVER_DEFAULT);
    }

    /**
     * Client-side of <tt>remoting</tt>, specifying how to access the server.
     * 
     * <p>
     * This lookup is called in three different contexts:
     * <ul>
     * <li>the <tt>NakedObjectsSystemFactoryUsingInstallers</tt> uses this to lookup the
     * {@link PersistenceMechanismInstaller} (may be a <tt>ProxyPersistor</tt>)</li>
     * <li>the <tt>NakedObjectsSystemFactoryUsingInstallers</tt> also uses this to lookup the
     * {@link FacetDecoratorInstaller}; adds in remoting facets.</li>
     * <li>the <tt>NakedObjectsSystemUsingInstallers</tt> uses this to lookup the
     * {@link AuthenticationManagerInstaller}.</li>
     * </ul>
     * 
     * <p>
     * In addition to the usual {@link #mergeConfigurationFor(Installer) merging} of any {@link Installer}
     * -specific configuration files, this lookup also merges in any
     * {@link ClientConnectionInstaller#getRemoteProperties() remote properties} available.
     */
    public ClientConnectionInstaller clientConnectionInstaller(final String requested) {
        return getInstaller(ClientConnectionInstaller.class, requested, SystemConstants.CLIENT_CONNECTION_KEY,
                SystemConstants.CLIENT_CONNECTION_DEFAULT);
    }

    public NakedObjectsViewerInstaller viewerInstaller(final String name, final String defaultName) {
        String viewer;
        if (name == null) {
            viewer = getConfiguration().getString(SystemConstants.VIEWER_KEY, defaultName);
        } else {
            viewer = name;
        }
        if (viewer == null) {
            return null;
        }
        return getInstaller(NakedObjectsViewerInstaller.class, viewer);
    }

    public NakedObjectsViewerInstaller viewerInstaller(final String name) {
        final NakedObjectsViewerInstaller installer = getInstaller(NakedObjectsViewerInstaller.class, name);
        if (installer == null) {
            throw new NakedObjectException("No viewer installer of type " + name);
        }
        return installer;
    }

    public ServicesInstaller servicesInstaller(final String requestedImplementationName) {
        return getInstaller(ServicesInstaller.class, requestedImplementationName, SystemConstants.SERVICES_INSTALLER_KEY,
                SystemConstants.SERVICES_INSTALLER_DEFAULT);
    }

    // ////////////////////////////////////////////////////////
    // Generic Lookups
    // ////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    public <T extends Installer> T getInstaller(final Class<T> cls, final String implName) {
        Assert.assertNotNull("No name specified", implName);
        for (final Installer installer : installerList) {
            if (cls.isAssignableFrom(installer.getClass()) && installer.getName().equals(implName)) {
                mergeConfigurationFor(installer);
                injectDependenciesInto(installer);
                return (T) installer;
            }
        }
        return (T) getInstaller(implName);
    }

    @SuppressWarnings("unchecked")
    public Installer getInstaller(final String implClassName) {
        try {
            Installer installer = CastUtils.cast(InstanceFactory.createInstance(implClassName));
            if (installer != null) {
                mergeConfigurationFor(installer);
                injectDependenciesInto(installer);
            }
            return installer;
        } catch (final InstanceCreationException e) {
            throw new InstanceCreationException("Specification error in " + INSTALLER_REGISTRY_FILE, e);
        } catch (final UnavailableClassException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Installer> T getInstaller(final Class<T> installerCls) {
        try {
            T installer = (T) (InstanceFactory.createInstance(installerCls));
            if (installer != null) {
                mergeConfigurationFor(installer);
                injectDependenciesInto(installer);
            }
            return installer;
        } catch (final InstanceCreationException e) {
            throw new InstanceCreationException("Specification error in " + INSTALLER_REGISTRY_FILE, e);
        } catch (final UnavailableClassException e) {
            return null;
        }
    }

    // ////////////////////////////////////////////////////////
    // Helpers
    // ////////////////////////////////////////////////////////

    private <T extends Installer> T getInstaller(Class<T> requiredType, String reqImpl, String key, String defaultImpl) {
        if (reqImpl == null) {
            reqImpl = getConfiguration().getString(key, defaultImpl);
        }
        if (reqImpl == null) {
            return null;
        }
        T installer = getInstaller(requiredType, reqImpl);
        if (installer == null) {
            throw new InstanceCreationException("Failed to load installer class " + reqImpl + " (of type "
                    + requiredType.getName());
        }
        return installer;
    }

    private void close(final BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                throw new NakedObjectException(e);
            }
        }
    }

    private InputStream getInstallerRegistryStream(final String componentFile, Class<?> cls) {
        final InputStream in = cls.getResourceAsStream("/" + componentFile);
        if (in == null) {
            throw new NakedObjectException("No resource found: " + componentFile);
        }
        return in;
    }

    // ////////////////////////////////////////////////////////
    // Configuration
    // ////////////////////////////////////////////////////////

    public NakedObjectConfiguration getConfiguration() {
        return configurationBuilder.getConfiguration();
    }

    public void mergeConfigurationFor(Installer installer) {
        for (String installerConfigResource : installer.getConfigurationResources()) {
            configurationBuilder.addConfigurationResource(installerConfigResource, NotFoundPolicy.CONTINUE);
        }
    }

    public <T> T injectDependenciesInto(T candidate) {
        injectInto(candidate);
        return candidate;
    }

    // ////////////////////////////////////////////////////////////////////
    // Injectable
    // ////////////////////////////////////////////////////////////////////

    public void injectInto(Object candidate) {
        if (InstallerLookupAware.class.isAssignableFrom(candidate.getClass())) {
            InstallerLookupAware cast = InstallerLookupAware.class.cast(candidate);
            cast.setInstallerLookup(this);
        }
        configurationBuilder.injectInto(candidate);
    }

    // ////////////////////////////////////////////////////////
    // Dependencies (injected)
    // ////////////////////////////////////////////////////////

    public ConfigurationBuilder getConfigurationBuilder() {
        return configurationBuilder;
    }

    public void setConfigurationBuilder(final ConfigurationBuilder configurationLoader) {
        this.configurationBuilder = configurationLoader;
    }

}

// Copyright (c) Naked Objects Group Ltd.
