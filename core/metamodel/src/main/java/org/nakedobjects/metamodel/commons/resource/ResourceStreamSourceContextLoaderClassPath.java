package org.nakedobjects.metamodel.commons.resource;

import java.io.InputStream;


/**
 * Loads the properties from the ContextClassLoader.
 * 
 * <p>
 * If this class is on the system class path, then the class loader obtained from this.getClassLoader() won't
 * be able to load resources from the application class path.
 */
public class ResourceStreamSourceContextLoaderClassPath extends ResourceStreamSourceAbstract {

    protected InputStream doReadResource(String resourcePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(resourcePath);
    }

    public String getName() {
        return "context loader classpath";
    }

}
