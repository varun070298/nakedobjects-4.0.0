package org.nakedobjects.metamodel.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceComposite;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceFileSystem;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.config.internal.PropertiesReader;


/**
 * Adapter for {@link ConfigurationBuilder}, loading the specified configuration resource (file) from the
 * given {@link ResourceStreamSource}(s).
 * 
 * <p>
 * If a property is in multiple configuration resources then the latter resources will overwrite the former.
 */
public class ConfigurationBuilderAbstract implements ConfigurationBuilder {

    private static final Logger LOG = Logger.getLogger(ConfigurationBuilderAbstract.class);

    static class ConfigurationResourceAndPolicy {
        private String configurationResource;
        private NotFoundPolicy notFoundPolicy;

        public ConfigurationResourceAndPolicy(String configurationResource, NotFoundPolicy notFoundPolicy) {
            this.configurationResource = configurationResource;
            this.notFoundPolicy = notFoundPolicy;
        }

        public String getConfigurationResource() {
            return configurationResource;
        }

        public NotFoundPolicy getNotFoundPolicy() {
            return notFoundPolicy;
        }
    }

    private final ResourceStreamSource resourceStreamSource;

    private final List<ConfigurationResourceAndPolicy> configurationResources = new ArrayList<ConfigurationResourceAndPolicy>();
    private final Properties additionalProperties = new Properties();
    private boolean includeSystemProperties = false;

    /**
     * Most recent snapshot of {@link NakedObjectConfiguration} obtained from {@link #configurationLoader}.
     * 
     * <p>
     * Whenever further configuration is merged in, this cache is invalidated.
     */
    private NakedObjectConfiguration cachedConfiguration;

    // ////////////////////////////////////////////////////////////
    // Constructor, initialization
    // ////////////////////////////////////////////////////////////

    public ConfigurationBuilderAbstract() {
        this(new ResourceStreamSourceFileSystem(ConfigurationConstants.DEFAULT_CONFIG_DIRECTORY));
    }

    public ConfigurationBuilderAbstract(final ResourceStreamSource resourceStreamSource) {
        this.resourceStreamSource = resourceStreamSource;
        addDefaultConfigurationResources();
    }

    public ConfigurationBuilderAbstract(final ResourceStreamSource... resourceStreamSources) {
        ResourceStreamSourceComposite composite = new ResourceStreamSourceComposite();
        for (ResourceStreamSource rss : resourceStreamSources) {
            composite.addResourceStreamSource(rss);
        }
        this.resourceStreamSource = composite;
        addDefaultConfigurationResources();
    }

    /**
     * May be overridden by subclasses if required.
     */
    protected void addDefaultConfigurationResources() {
        addConfigurationResource(ConfigurationConstants.DEFAULT_CONFIG_FILE, NotFoundPolicy.FAIL_FAST);
        addConfigurationResource(ConfigurationConstants.WEB_CONFIG_FILE, NotFoundPolicy.CONTINUE);
    }

    // ////////////////////////////////////////////////////////////
    // ResourceStreamSource
    // ////////////////////////////////////////////////////////////

    public ResourceStreamSource getResourceStreamSource() {
        return resourceStreamSource;
    }

    // ////////////////////////////////////////////////////////////
    // populating or updating
    // ////////////////////////////////////////////////////////////

    /**
     * Registers the configuration resource (usually, a file) with the specified name from the first
     * {@link ResourceStreamSource} available.
     * 
     * <p>
     * If the configuration resource cannot be found then the provided {@link NotFoundPolicy} determines
     * whether an exception is thrown or not.
     * 
     * <p>
     * Must be called before {@link #getConfiguration()}; the resource is actually read on
     * {@link #getConfiguration()}.
     */
    public synchronized void addConfigurationResource(final String configurationResource, final NotFoundPolicy notFoundPolicy) {
        configurationResources.add(new ConfigurationResourceAndPolicy(configurationResource, notFoundPolicy));
        invalidateCache();
    }

    public synchronized void setIncludeSystemProperties(final boolean includeSystemProperties) {
        this.includeSystemProperties = includeSystemProperties;
        invalidateCache();
    }

    /**
     * Adds additional property.
     */
    public synchronized void add(final String key, final String value) {
        if (key == null || value == null) {
            return;
        }
        additionalProperties.setProperty(key, value);
        if (LOG.isInfoEnabled()) {
            LOG.info("added " + key + "=" + value);
        }
        invalidateCache();
    }

    /**
     * Adds additional properties.
     */
    public synchronized void add(final Properties properties) {
        final Enumeration<?> keys = properties.propertyNames();
        while (keys.hasMoreElements()) {
            final String key = (String) keys.nextElement();
            add(key, properties.getProperty(key));
        }
        invalidateCache();
    }

    // ////////////////////////////////////////////////////////////
    // getConfiguration
    // ////////////////////////////////////////////////////////////

    /**
     * Returns the current {@link NakedObjectConfiguration configuration}.
     */
    public synchronized NakedObjectConfiguration getConfiguration() {
        if (cachedConfiguration != null) {
            return cachedConfiguration;
        }

        final PropertiesConfiguration configuration = new PropertiesConfiguration(getResourceStreamSource());
        loadConfigurationResources(configuration);
        // TODO: this hack should move elsewhere, where the DeploymentType is
        // known.
        addShowExplorationOptionsIfNotSpecified(configuration);
        addSystemPropertiesIfRequested(configuration);
        addAdditionalProperties(configuration);
        return cachedConfiguration = configuration;
    }

    private void loadConfigurationResources(PropertiesConfiguration configuration) {
        for (ConfigurationResourceAndPolicy configResourceAndPolicy : configurationResources) {
            loadConfigurationResource(configuration, configResourceAndPolicy);
        }
    }

    private void loadConfigurationResource(
            PropertiesConfiguration configuration,
            ConfigurationResourceAndPolicy configResourceAndPolicy) {
        String configurationResource = configResourceAndPolicy.getConfigurationResource();
        NotFoundPolicy notFoundPolicy = configResourceAndPolicy.getNotFoundPolicy();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("loading configuration resource: " + configurationResource + ", notFoundPolicy: " + notFoundPolicy);
        }
        loadConfigurationResource(configuration, configurationResource, notFoundPolicy);
    }

    /**
     * Loads the configuration resource (usually, a file) with the specified name from the first
     * {@link ResourceStreamSource} available.
     * 
     * <p>
     * If the configuration resource cannot be found then the provided {@link NotFoundPolicy} determines
     * whether an exception is thrown or not.
     */
    protected void loadConfigurationResource(
            final PropertiesConfiguration configuration,
            final String configurationResource,
            final NotFoundPolicy notFoundPolicy) {
        try {
            PropertiesReader propertiesReader = loadConfigurationResource(resourceStreamSource, configurationResource);
            addProperties(configuration, propertiesReader.getProperties());
            if (LOG.isInfoEnabled()) {
                LOG.info("'" + configurationResource + "' FOUND");
            }
            return;
        } catch (IOException ex) {
            // keep going
        }
        if (notFoundPolicy == NotFoundPolicy.FAIL_FAST) {
            throw new NakedObjectException("failed to load '" + configurationResource + "'; tried using: "
                    + resourceStreamSource.getName());
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("'" + configurationResource + "' not found, but not needed");
            }
        }
    }

    private PropertiesReader loadConfigurationResource(
            ResourceStreamSource resourceStreamSource,
            final String configurationResource) throws IOException {
        return new PropertiesReader(resourceStreamSource, configurationResource);
    }

    private void addShowExplorationOptionsIfNotSpecified(PropertiesConfiguration configuration) {
        if (configuration.getString(ConfigurationConstants.SHOW_EXPLORATION_OPTIONS) == null) {
            configuration.add(ConfigurationConstants.SHOW_EXPLORATION_OPTIONS, "yes");
        }
    }

    private void addSystemPropertiesIfRequested(PropertiesConfiguration configuration) {
        if (includeSystemProperties) {
            addProperties(configuration, System.getProperties());
        }
    }

    private void addAdditionalProperties(PropertiesConfiguration configuration) {
        addProperties(configuration, additionalProperties);
    }

    protected void addProperties(PropertiesConfiguration configuration, Properties properties) {
        configuration.add(properties);
    }

    private void invalidateCache() {
        cachedConfiguration = null;
    }

    // ////////////////////////////////////////////////////////////
    // Injectable
    // ////////////////////////////////////////////////////////////

    public void injectInto(Object candidate) {
        if (ConfigurationBuilderAware.class.isAssignableFrom(candidate.getClass())) {
            ConfigurationBuilderAware cast = ConfigurationBuilderAware.class.cast(candidate);
            cast.setConfigurationBuilder(this);
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
