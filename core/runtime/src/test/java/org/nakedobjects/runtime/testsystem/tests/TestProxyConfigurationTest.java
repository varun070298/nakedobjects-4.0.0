package org.nakedobjects.runtime.testsystem.tests;

import junit.framework.TestCase;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.testsystem.TestProxyConfiguration;


public class TestProxyConfigurationTest extends TestCase {

    private TestProxyConfiguration configuration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        configuration = new TestProxyConfiguration();
        configuration.add("test.one", "abc");
        configuration.add("test.two", "10");
        configuration.add("another.one", "ghi");
    }

    public void testGetString() {
        assertEquals("abc", configuration.getString("test.one"));
    }

    public void testGetNumber() throws Exception {
        assertEquals(10, configuration.getInteger("test.two"));
    }

    public void testSubset() throws Exception {
        final NakedObjectConfiguration properties = configuration.getProperties("test.");
        assertEquals("abc", properties.getString("one"));
    }
}

// Copyright (c) Naked Objects Group Ltd.
