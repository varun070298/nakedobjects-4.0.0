package org.nakedobjects.runtime.userprofile;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;


public class Options implements DebugInfo {
    private final Properties properties = new Properties();

    public void addOption(String name, String value) {
        properties.put(name, value);
    }

    public void copy(Options options) {
        properties.putAll(options.properties);
    }

    public void debugData(DebugString debug) {
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            debug.appendln(name, properties.get(name));
        }
    }

    public Iterator<String>  names() {
    	final Enumeration<?> propertyNames = properties.propertyNames();
    	return new Iterator<String>() {
			public boolean hasNext() {
				return propertyNames.hasMoreElements();
			}
			public String next() {
				return (String) propertyNames.nextElement();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}};
    }
    
    public String debugTitle() {
        return "Options";
    }

    public String value(String name) {
        return properties.getProperty(name);
    }

}

// Copyright (c) Naked Objects Group Ltd.
