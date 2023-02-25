package org.nakedobjects.metamodel.commons.resource;

import java.io.InputStream;
import java.io.OutputStream;

public interface ResourceStreamSource {
	
	/**
	 * Returns an {@link InputStream} opened for the resource path, or
	 * <tt>null</tt> otherwise.
	 */
	public InputStream readResource(String resourcePath);
	
	/**
	 * Returns an {@link OutputStream} opened to write to the resource,
	 * or <tt>null</tt> otherwise.
	 */
	public OutputStream writeResource(String resourcePath);

	public String getName();

}
