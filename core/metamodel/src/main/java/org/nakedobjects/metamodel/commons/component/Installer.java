package org.nakedobjects.metamodel.commons.component;

import java.util.List;

/**
 * A factory for a component, used during it boot strapping process.
 * 
 * <p>
 * All installers listed (by class name) in
 * <tt>installer-registry.properties</tt> are loaded when the boot strap class
 * is initially loaded. Then named installers are used during boot to create
 * components for the system. The name method specified is the name that the
 * component can be installed by.
 */
public interface Installer extends ApplicationScopedComponent {

	/**
	 * The type of the installer, meaning the component type, and consistent
	 * with the long option of the command line flag where applicable.
	 * 
	 * <p>
	 * Examples are <tt>authentication</tt> or <tt>persistor</tt>.
	 * 
	 * <p>
	 * Because all implementations of a given subinterface of {@link Installer}
	 * should return the same value for this method, by convention these
	 * subinterfaces define a constant which the implementation can just return.
	 * 
	 * <p>
	 * Used, with {@link #getName()}, to determine the config files and config
	 * keys for this installer.
	 * 
	 * @see #getConfigurationResources()
	 */
	String getType();

	/**
	 * The name (qualified by type).
	 * 
	 * <p>
	 * Used, with {@link #getType()}, to determine the config files and config
	 * keys for this installer.
	 * 
	 * @see #getConfigurationResources()
	 */
	String getName();

	/**
	 * The configuration resources (files) to merge in configuration properties.
	 * 
	 * <p>
	 * For example, would return list of [<tt>persistor.properties</tt>,
	 * and <tt>persistor_in-memory.properties</tt>] for the in-memory
	 * object store.
	 * 
	 * <p>
	 * The implementation should look under keys prefixed either
	 * <tt>nakedobjects.persistor</tt> or
	 * <tt>nakedobjects.persistor.in-memory</tt>.
	 * 
	 * <p>
	 * Note that we use an '_' underscore to join the {@link #getType() type}
	 * and {@link #getName() name} in the filenames, but a '.' (period) for the
	 * keys.
	 */
	List<String> getConfigurationResources();
}

// Copyright (c) Naked Objects Group Ltd.
