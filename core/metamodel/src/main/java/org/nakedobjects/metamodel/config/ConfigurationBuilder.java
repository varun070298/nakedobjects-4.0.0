package org.nakedobjects.metamodel.config;

import java.util.Properties;

import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceComposite;

/**
 * Holds a mutable set of properties representing the configuration.
 * 
 * <p>
 * Mutable/immutable pair with the {@link NakedObjectConfiguration}. To obtain
 * the configuration, use {@link #getConfiguration()}.
 * 
 * @see NakedObjectConfiguration for more details on the mutable/immutable pair
 *      pattern.
 */
public interface ConfigurationBuilder extends Injectable {

	/**
	 * Returns a currently known {@link NakedObjectConfiguration}.
	 */
    NakedObjectConfiguration getConfiguration();

	public abstract void addConfigurationResource(final String installerName,
			final NotFoundPolicy notFoundPolicy);

	public abstract void add(final Properties properties);

	public abstract void add(final String key, final String value);

	/**
	 * The underlying {@link ResourceStreamSource} from which the configuration
	 * is being read.
	 * 
	 * <p>
	 * Note that this may be a {@link ResourceStreamSourceComposite composite}.
	 */
	public ResourceStreamSource getResourceStreamSource();
}
// Copyright (c) Naked Objects Group Ltd.
