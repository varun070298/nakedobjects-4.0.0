package org.nakedobjects.metamodel.commons.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public abstract class ResourceStreamSourceAbstract implements ResourceStreamSource {
	
	private static Logger LOG = Logger.getLogger(ResourceStreamSourceAbstract.class);
	
	public final InputStream readResource(String resourcePath) {
		
		try {
			InputStream resourceStream = doReadResource(resourcePath);
			if (resourceStream != null) {
				return resourceStream;
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("could not load resource path '" + resourcePath + "' from " + getName() );
			}
		} catch (IOException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("could not load resource path '" + resourcePath + "' from " + getName() + ", exception: " + e.getMessage() );
			}
		}
		return null;
	}
	
	/**
	 * Mandatory hook method; subclasses can return either <tt>null</tt> or throw
	 * an exception if the resource could not be found.
	 */
	protected abstract InputStream doReadResource(String resourcePath) throws IOException;

	/**
	 * Default implementation returns <tt>null</tt> (that is, not supported).
	 */
	public OutputStream writeResource(String resourcePath) {
		return null;
	}


}
