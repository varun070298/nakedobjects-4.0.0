package org.nakedobjects.metamodel.config;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceContextLoaderClassPath;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceFileSystem;

/**
 * Convenience implementation of {@link ConfigurationBuilder} that
 * loads configuration resource as per {@link ConfigurationBuilderFileSystem}
 * and otherwise from the {@link ResourceStreamSourceContextLoaderClassPath classpath}.
 * 
 * @see ResourceStreamSourceFileSystem
 */
public class ConfigurationBuilderDefault extends ConfigurationBuilderAbstract {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(ConfigurationBuilderDefault.class);
	
	public ConfigurationBuilderDefault() {
		super(
				new ResourceStreamSourceFileSystem(ConfigurationConstants.DEFAULT_CONFIG_DIRECTORY),
				new ResourceStreamSourceFileSystem(ConfigurationConstants.WEBINF_CONFIG_DIRECTORY),
				new ResourceStreamSourceContextLoaderClassPath()
			);
	}



}
// Copyright (c) Naked Objects Group Ltd.
