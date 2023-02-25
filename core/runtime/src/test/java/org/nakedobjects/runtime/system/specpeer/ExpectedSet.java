package org.nakedobjects.runtime.system.specpeer;

import java.util.Vector;

import junit.framework.Assert;


@SuppressWarnings("unchecked")
public class ExpectedSet {
	
	private final Vector expectedObjects = new Vector();
    private final Vector actualObjects = new Vector();

    public void addActual(final Object object) {
        actualObjects.addElement(object);
        Assert.assertTrue("More actuals than expected; only expected " + expectedObjects.size(),
                actualObjects.size() <= expectedObjects.size());
        Assert.assertEquals("Actual does not match expected.\n", expectedObjects.elementAt(actualObjects.size() - 1), object);
    }

    public void addExpected(final Object object) {
        expectedObjects.addElement(object);
    }

    public void verify() {
        Assert.assertTrue("Too few actuals added\n  Expected " + expectedObjects + "\n  but got " + actualObjects, actualObjects
                .size() == expectedObjects.size());
    }
}
// Copyright (c) Naked Objects Group Ltd.
