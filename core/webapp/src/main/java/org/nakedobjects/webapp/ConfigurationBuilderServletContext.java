package org.nakedobjects.webapp;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceFileSystem;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.ConfigurationBuilderAbstract;
import org.nakedobjects.metamodel.config.ConfigurationBuilderFileSystem;

/**
 * Convenience implementation of {@link ConfigurationBuilder} that
 * loads configuration resource from a specified directory on the
 * filesystem.
 * 
 * @see ResourceStreamSourceFileSystem
 */
public class ConfigurationBuilderServletContext extends
		ConfigurationBuilderAbstract {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(ConfigurationBuilderFileSystem.class);
	
	public ConfigurationBuilderServletContext(final ServletContext servletContext) {
		super(new ResourceStreamSourceServletContext(servletContext));
	}

}
