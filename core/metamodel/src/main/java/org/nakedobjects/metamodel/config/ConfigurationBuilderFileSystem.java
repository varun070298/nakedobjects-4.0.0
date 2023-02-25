package org.nakedobjects.metamodel.config;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceComposite;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceFileSystem;

/**
 * Convenience implementation of {@link ConfigurationBuilder} that
 * loads configuration resource from a specified directory (or directories) on the
 * filesystem.
 * 
 * @see ResourceStreamSourceFileSystem
 */
public class ConfigurationBuilderFileSystem extends ConfigurationBuilderAbstract {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(ConfigurationBuilderFileSystem.class);
	
	private static ResourceStreamSource createResourceStreamSource(
			String... directories) {
		ResourceStreamSourceComposite composite = new ResourceStreamSourceComposite();
		for(String directory: directories) {
			composite.addResourceStreamSource(
				new ResourceStreamSourceFileSystem(directory));
		}
		return composite;
	}

	public ConfigurationBuilderFileSystem(String... directories) {
		super(createResourceStreamSource(directories));
	}

	public ConfigurationBuilderFileSystem() {
		super(new ResourceStreamSourceFileSystem(ConfigurationConstants.DEFAULT_CONFIG_DIRECTORY));
	}

}
// Copyright (c) Naked Objects Group Ltd.
