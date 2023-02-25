package org.nakedobjects.webapp;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.nakedobjects.metamodel.commons.lang.PathUtils;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSourceAbstract;

public class ResourceStreamSourceServletContext extends ResourceStreamSourceAbstract {

	public static final String DEFAULT_WEBINF_DIRECTORY = "/WEB-INF";
	
	private final ServletContext servletContext;
	private final String configurationDirectory;

	public ResourceStreamSourceServletContext(ServletContext servletContext) {
		this(servletContext, DEFAULT_WEBINF_DIRECTORY);
	}
	
	public ResourceStreamSourceServletContext(ServletContext servletContext, String configurationDirectory) {
		this.servletContext = servletContext;
		this.configurationDirectory = configurationDirectory;
	}
	
	public String getName() {
		return "servlet context ('" + configurationDirectory + "')";
	}

	public InputStream doReadResource(String resourcePath) throws IOException {
		String fullyQualifiedResourcePath = PathUtils.combine(configurationDirectory, resourcePath);
		return servletContext.getResourceAsStream(fullyQualifiedResourcePath);
	}

}
