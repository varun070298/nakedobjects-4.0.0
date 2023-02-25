package org.nakedobjects.application.valueholder;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;


public class WholeNumberTests extends ValueTestCase {

    public static void main(final String[] args) {
        LogManager.getLoggerRepository().setThreshold(Level.OFF);
        TestRunner.run(new TestSuite(WholeNumberTests.class));
    }

    public void testEquals() throws NoSuchMethodException {
        WholeNumber one = new WholeNumber(189);
        WholeNumber two = new WholeNumber(189);
        WholeNumber three = new WholeNumber(1890);

        assertEquals(one, two);
        assertEquals(two, one);
        assertTrue(!one.equals(three));
    }
}
// Copyright (c) Naked Objects Group Ltd.
