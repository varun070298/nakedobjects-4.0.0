package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.system.TestClock;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public abstract class ValueTestCase extends TestCase {
    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        // new MockNakedObjectSpecificationLoader();
        new TestClock();
    }
}
// Copyright (c) Naked Objects Group Ltd.
