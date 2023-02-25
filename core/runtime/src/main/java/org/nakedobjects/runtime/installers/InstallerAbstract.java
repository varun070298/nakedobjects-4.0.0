package org.nakedobjects.runtime.installers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.commons.component.Installer;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderAware;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;

public abstract class InstallerAbstract implements Installer,
		ConfigurationBuilderAware {

	private final String type;
	private final String name;
	
	private ConfigurationBuilder configurationBuilder;
	private NakedObjectConfiguration configuration;

	/**
	 * Subclasses should pass in the type defined as a constant in the
	 * subinterface of Installer.
	 * 
	 * <p>
	 * For example, {@link PersistenceMechanismInstaller} has a constant
	 * {@link PersistenceMechanismInstaller#TYPE}. Any implementation of
	 * {@link PersistenceMechanismInstaller} should pass this constant value up
	 * to this constructor.
	 */
	public InstallerAbstract(final String type, final String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns <tt>[type.properties, type_name.properties</tt>.
	 * 
	 * <p>
	 * For example, <tt>[persistor.properties, persistor_in-memory.properties]</tt>.
	 * 
	 * @see #getType()
	 * @see #getName()
	 */
	public List<String> getConfigurationResources() {
		ArrayList<String> resourceList = new ArrayList<String>();
		String componentFile = getType() + ".properties";
		resourceList.add(componentFile);
		String componentImplementationFile = getType() + "_" + getName() + ".properties";
		resourceList.add(componentImplementationFile);
		addConfigurationResources(resourceList);
		return Collections.unmodifiableList(resourceList);
	}

	/**
	 * Optional hook method to allow subclasses to specify any additional config resources.
	 */
	protected void addConfigurationResources(List<String> configurationResources) {
	}

	//////////////////////////////////////////////////////
	// init, shutdown
	//////////////////////////////////////////////////////
	
	/**
	 * Default implementation does nothing.
	 */
	public void init() {
		// no-op implementation, subclasses may override!
	}

	/**
	 * Default implementation does nothing.
	 */
	public void shutdown() {
		// no-op implementation, subclasses may override!
	}


	/**
	 * Either this method or {@link #setConfiguration(NakedObjectConfiguration)}
	 * should be called prior to calling {@link #getConfiguration()}.
	 * 
	 * <p>
	 * If a {@link #setConfiguration(NakedObjectConfiguration) configuration}
	 * has already been provided, then throws {@link IllegalStateException}.
	 */
	public void setConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		if (configuration != null) {
			throw new IllegalStateException(
					"A NakedObjectConfiguration has already been provided.");
		}
		this.configurationBuilder = configurationBuilder;
	}

	/**
	 * Either this method or
	 * {@link #setConfigurationBuilder(ConfigurationBuilder)} should be called
	 * prior to calling {@link #getConfiguration()}.
	 * 
	 * <p>
	 * If a {@link #setConfigurationBuilder(ConfigurationBuilder) configuration
	 * builder} has already been provided, then throws
	 * {@link IllegalStateException}.
	 */
	public void setConfiguration(NakedObjectConfiguration configuration) {
		if (configurationBuilder != null) {
			throw new IllegalStateException(
					"A NakedObjectConfiguration has already been provided.");
		}
		this.configuration = configuration;
	}

	/**
	 * Returns a <i>snapshot</i> of the current configuration provided by the
	 * {@link #setConfigurationBuilder(ConfigurationBuilder) injected}
	 * {@link ConfigurationBuilder}.
	 * 
	 * <p>
	 * Implementation note: the implementation is in fact just
	 * {@link InstallerLookupDefault}.
	 */
	public NakedObjectConfiguration getConfiguration() {
		if (configurationBuilder != null) {
			return configurationBuilder.getConfiguration();
		} else if (configuration != null) {
			return configuration;
		} else {
			throw new IllegalStateException(
					"Neither a ConfigurationBuilder nor Configuration has not been provided");
		}
	}
}

// Copyright (c) Naked Objects Group Ltd.
