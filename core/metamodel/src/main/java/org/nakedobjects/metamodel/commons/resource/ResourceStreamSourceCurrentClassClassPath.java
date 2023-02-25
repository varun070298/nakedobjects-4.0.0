package org.nakedobjects.metamodel.commons.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Load from this class' ClassLoader.
 * 
 *<p>
 * This is useful if (a) the contextClassLoader is not available and (b) the
 * contextClassLoader does not load from this classes ClassLoader (for example
 * when running under Ant while running unit tests as part of an automated
 * build).
 */
public class ResourceStreamSourceCurrentClassClassPath extends
		ResourceStreamSourceAbstract {

	protected InputStream doReadResource(String resourcePath) throws IOException {
		return getClass().getClassLoader().getResourceAsStream(resourcePath);
	}

	public String getName() {
		return "current class' classpath";
	}

}
