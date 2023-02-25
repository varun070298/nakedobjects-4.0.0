package org.nakedobjects.runtime.testsystem;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;

import junit.framework.AssertionFailedError;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;


public class TestProxyConfiguration implements NakedObjectConfiguration {

    private final Hashtable<String,String> valueByKey = new Hashtable<String,String>();

    public void add(final String key, final String value) {
        if (key == null) {
            return;
        }
        if (valueByKey.containsKey(key)) {
            throw new AssertionFailedError("Already have a value for " + " name; cannot set it again: " + value);
        }
        valueByKey.put(key, value);
    }

    public boolean getBoolean(final String name) {
        return Boolean.valueOf(getString(name)).booleanValue();
    }

    public boolean getBoolean(final String name, final boolean defaultValue) {
        final String str = getString(name);
        return str == null ? defaultValue : Boolean.valueOf(str).booleanValue();
    }

    public Color getColor(final String name) {
        throw new NotYetImplementedException();
    }

    public Color getColor(final String name, final Color defaultColor) {
        return defaultColor;
    }

    public Font getFont(final String name) {
        throw new NotYetImplementedException();
    }

    public Font getFont(final String name, final Font defaultValue) {
        return defaultValue;
    }

    public int getInteger(final String name) {
        return Integer.valueOf(getString(name)).intValue();
    }

    public int getInteger(final String name, final int defaultValue) {
        final String str = getString(name);
        return str == null ? defaultValue : Integer.valueOf(str).intValue();
    }

    public String[] getList(final String name) {
        return new String[0];
    }

    public NakedObjectConfiguration getProperties(final String withPrefix) {
        final TestProxyConfiguration configuration = new TestProxyConfiguration();
        final Enumeration<String> keys = valueByKey.keys();
        while (keys.hasMoreElements()) {
            final String key = (String) keys.nextElement();
            if (key.startsWith(withPrefix)) {
                configuration.add(key.substring(withPrefix.length()), (String) valueByKey.get(key));
            }

        }
        return configuration;
    }

    public String getString(final String name) {
        return (String) valueByKey.get(name);
    }

    public String getString(final String name, final String defaultValue) {
        final String str = getString(name);
        return str == null ? defaultValue : str;
    }

    public boolean hasProperty(final String name) {
        throw new NotYetImplementedException();
    }

    public String referedToAs(final String name) {
        throw new NotYetImplementedException();
    }

    public NakedObjectConfiguration createSubset(final String prefix) {
        throw new NotYetImplementedException();
    }

    public boolean isEmpty() {
        throw new NotYetImplementedException();
    }

    public int size() {
        throw new NotYetImplementedException();
    }

    public Enumeration<String> propertyNames() {
        throw new NotYetImplementedException();
    }

	public ResourceStreamSource getResourceStreamSource() {
		return null;
	}

    public void debugData(final DebugString debug) {}

    public String debugTitle() {
        throw new NotYetImplementedException();
    }

    public void injectInto(Object candidate) {}


}
// Copyright (c) Naked Objects Group Ltd.
