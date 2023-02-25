package org.nakedobjects.metamodel.spec.feature;

import junit.framework.TestCase;


public class ActionTargetTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ActionTargetTest.class);
    }

    public void testEquals() {
        assertTrue(NakedObjectActionConstants.EXPLORATION.equals(NakedObjectActionConstants.EXPLORATION));
    }

    public void testHashCode() {
        assertEquals(NakedObjectActionConstants.EXPLORATION.hashCode(), NakedObjectActionConstants.EXPLORATION.hashCode());
    }

}
// Copyright (c) Naked Objects Group Ltd.
