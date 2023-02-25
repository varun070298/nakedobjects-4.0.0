package org.nakedobjects.metamodel.examples.facets.namefile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class NameFileParser {

    private static final String CONFIG_NAMEFILE_PROPERTIES = "config/namefile.properties";
    private Properties properties;

    public void parse() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NAMEFILE_PROPERTIES);
        if (in == null) {
            in = getClass().getClassLoader().getResourceAsStream(CONFIG_NAMEFILE_PROPERTIES); 
        }
        if (in == null) {
            throw new NullPointerException("Cannot locate resource '" + CONFIG_NAMEFILE_PROPERTIES + "'");
        }
        properties = new Properties();
        properties.load(in);
    }

    public String getName(final Class<?> cls) {
        return properties.getProperty(cls.getCanonicalName());
    }

    public String getMemberName(final Class<?> cls, final String memberName) {
        return properties.getProperty(cls.getCanonicalName()+"#"+memberName);
    }
    
    
}
