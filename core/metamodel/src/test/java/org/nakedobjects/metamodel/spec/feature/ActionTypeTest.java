package org.nakedobjects.metamodel.spec.feature;

import junit.framework.TestCase;


public class ActionTypeTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ActionTypeTest.class);
    }

    public void testEquals() {
        assertTrue(NakedObjectActionConstants.LOCAL.equals(NakedObjectActionConstants.LOCAL));
    }

    public void testHashCode() {
        assertEquals(NakedObjectActionConstants.LOCAL.hashCode(), NakedObjectActionConstants.LOCAL.hashCode());
    }

}
// Copyright (c) Naked Objects Group Ltd.
