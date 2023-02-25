package org.nakedobjects.metamodel.config.loader;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.ConfigurationBuilderFileSystem;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NotFoundPolicy;


public class ConfigurationBuilderTwoDirectoriesTest extends TestCase {
    ConfigurationBuilderFileSystem builder;

    @Override
    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        builder = new ConfigurationBuilderFileSystem("src/test/config", "src/test");
    }

    public void testAddConfigurationInDifferentDirectory() throws Exception {
        builder.addConfigurationResource("three.properties", NotFoundPolicy.FAIL_FAST);
        final NakedObjectConfiguration configuration = builder.getConfiguration();
        assertEquals("version 3", configuration.getString("different.property"));
    }
}

// Copyright (c) Naked Objects Group Ltd.
