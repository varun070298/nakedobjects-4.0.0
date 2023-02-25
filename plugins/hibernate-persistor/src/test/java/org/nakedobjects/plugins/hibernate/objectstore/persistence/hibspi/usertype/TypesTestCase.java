package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import junit.framework.TestCase;

import org.hibernate.usertype.UserType;


public abstract class TypesTestCase extends TestCase {
    public static String[] names = { "name1" };

    public void basicTest(final UserType type) {
        final String test = "a";

        assertTrue("deep copy ==", test == type.deepCopy(test));
        assertTrue("assemble", test == type.assemble(test, null));
        assertTrue("disassemble", test == type.disassemble(test));
        assertTrue("replace", test == type.replace(test, null, null));
        assertTrue("!isMutable", !type.isMutable());
        assertTrue("equals", type.equals(test, test));
        assertTrue("!equals", !type.equals(test, "b"));
        assertEquals("hash", test.hashCode(), type.hashCode(test));
    }
}
// Copyright (c) Naked Objects Group Ltd.
