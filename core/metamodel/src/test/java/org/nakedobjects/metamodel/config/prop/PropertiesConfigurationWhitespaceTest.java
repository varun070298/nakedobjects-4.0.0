package org.nakedobjects.metamodel.config.prop;

import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;


public class PropertiesConfigurationWhitespaceTest extends TestCase {

    private PropertiesConfiguration configuration;

    public PropertiesConfigurationWhitespaceTest(final String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.OFF);

        configuration = new PropertiesConfiguration();

        final Properties p = new Properties();
        p.put("properties.leadingSpaces", "  twoSpacesBeforeThis");
        p.put("properties.leadingTab", "\toneTabBeforeThis");
        p.put("properties.trailingSpaces", "twoSpacesAfterThis  ");
        p.put("properties.trailingTab", "oneTabAfterThis\t");
        p.put("properties.trailingTabAndSpaces", "oneTabAndTwoSpacesAfterThis\t  ");
        configuration.add(p);

    }

    public void testLeadingSpaces() {
        assertEquals("twoSpacesBeforeThis", configuration.getString("properties.leadingSpaces"));
    }

    public void testLeadingTab() {
        assertEquals("oneTabBeforeThis", configuration.getString("properties.leadingTab"));
    }

    public void testTrailingSpaces() {
        assertEquals("twoSpacesAfterThis", configuration.getString("properties.trailingSpaces"));
    }

    public void testTrailingTab() {
        assertEquals("oneTabAfterThis", configuration.getString("properties.trailingTab"));
    }

    public void testTrailingTabSpaces() {
        assertEquals("oneTabAndTwoSpacesAfterThis", configuration.getString("properties.trailingTabAndSpaces"));
    }

}
// Copyright (c) Naked Objects Group Ltd.
