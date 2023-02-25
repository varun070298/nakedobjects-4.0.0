package org.nakedobjects.metamodel.spec;

import junit.framework.TestCase;


public class PersistableTest extends TestCase {

    private Persistability persistable;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testToStringForProgramPersistable() {
        persistable = Persistability.PROGRAM_PERSISTABLE;
        assertEquals("Program Persistable", persistable.toString());
    }

    public void testToStringForTransient() {
        persistable = Persistability.TRANSIENT;
        assertEquals("Transient", persistable.toString());
    }

    public void testToStringForUserPersistable() {
        persistable = Persistability.USER_PERSISTABLE;
        assertEquals("User Persistable", persistable.toString());
    }

}

// Copyright (c) Naked Objects Group Ltd.
