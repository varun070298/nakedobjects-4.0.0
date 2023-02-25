package org.nakedobjects.metamodel.config.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.nakedobjects.metamodel.commons.lang.IoUtils;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;


/**
 * This class loads properties using the specified {@link ResourceStreamSource}.
 */
public class PropertiesReader {

    private final Properties properties = new Properties();

    public PropertiesReader(final ResourceStreamSource resourceStream, final String configurationResource) throws IOException {

        InputStream in = null;
        try {
            in = resourceStream.readResource(configurationResource);
            if (in == null) {
                throw new IOException("Unable to find resource " + configurationResource);
            }
            properties.load(in);
        } finally {
            IoUtils.closeSafely(in);
        }
    }

    public Properties getProperties() {
        return properties;
    }

}
// Copyright (c) Naked Objects Group Ltd.
