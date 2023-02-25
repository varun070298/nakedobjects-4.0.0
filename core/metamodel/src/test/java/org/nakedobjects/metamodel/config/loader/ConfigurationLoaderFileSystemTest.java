package org.nakedobjects.metamodel.config.loader;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.ConfigurationBuilderFileSystem;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NotFoundPolicy;


public class ConfigurationLoaderFileSystemTest extends TestCase {
    ConfigurationBuilderFileSystem loader;

    @Override
    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        loader = new ConfigurationBuilderFileSystem("src/test/config");
    }

    public void testDefaultConfiguration() {
        final NakedObjectConfiguration configuration = loader.getConfiguration();
        assertEquals("one", configuration.getString("properties.example"));
    }

    public void testDefaultConfigurationTrailingWhitespace() {
        final NakedObjectConfiguration configuration = loader.getConfiguration();
        assertEquals("in-memory", configuration.getString("properties.trailingWhitespace"));
    }

    public void testAddConfiguration() {
        loader.addConfigurationResource("another.properties", NotFoundPolicy.FAIL_FAST);
        final NakedObjectConfiguration configuration = loader.getConfiguration();
        assertEquals("added", configuration.getString("additional.example"));
    }

    public void testAddedConfigurationOveridesEarlierProperties() {
        loader.addConfigurationResource("another.properties", NotFoundPolicy.FAIL_FAST);
        final NakedObjectConfiguration configuration = loader.getConfiguration();
        assertEquals("two", configuration.getString("properties.example"));
    }

    public void testAddedConfigurationFailsWhenFileNotFound() {
        try {
            loader.addConfigurationResource("unfound.properties", NotFoundPolicy.FAIL_FAST);
            loader.getConfiguration();
            fail();
        } catch (final NakedObjectException expected) {}
    }

    public void testAddedConfigurationIgnoreUnfoundFile() {
        loader.addConfigurationResource("unfound.properties", NotFoundPolicy.CONTINUE);
        loader.getConfiguration();
    }

    public void testAddProperty() throws Exception {
        loader.add("added.property", "added by code");
        final NakedObjectConfiguration configuration = loader.getConfiguration();
        assertEquals("added by code", configuration.getString("added.property"));
    }

    public void testIncludeSystemProperty() throws Exception {
        loader.setIncludeSystemProperties(true);
        final NakedObjectConfiguration configuration = loader.getConfiguration();
        assertEquals(System.getProperty("os.name"), configuration.getString("os.name"));
    }
}

// Copyright (c) Naked Objects Group Ltd.
