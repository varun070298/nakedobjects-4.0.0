package org.nakedobjects.runtime.memento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;


public class MementoTest extends ProxyJunit4TestCase {

    private NakedObject originalAdapter;
    private NakedObject returnedAdapter;

    @Before
    public void setUp() throws Exception {

        originalAdapter = system.createAdapterForTransient(new TestPojoSimple("fred"));
        final Memento memento = new Memento(originalAdapter);
        system.resetLoader();
        returnedAdapter = memento.recreateObject();
    }

    @Test
    public void testDifferentAdapterReturned() throws Exception {
        assertNotSame(originalAdapter, returnedAdapter);
    }

    @Test
    public void testHaveSameOid() throws Exception {
        assertEquals(originalAdapter.getOid(), returnedAdapter.getOid());
    }

    @Test
    public void testHaveSameSpecification() throws Exception {
        assertEquals(originalAdapter.getSpecification(), returnedAdapter.getSpecification());
    }

    //@Ignore("TODO need to add reflective code to test system for this to work")
    @Test
    public void testName() throws Exception {
        @SuppressWarnings("unused")
        final TestPojoSimple originalPojo = (TestPojoSimple) originalAdapter.getObject();
        @SuppressWarnings("unused")
        final TestPojoSimple returnedPojo = (TestPojoSimple) returnedAdapter.getObject();

        // assertEquals(originalPojo.getName(), returnedPojo.getName());
    }

}

// Copyright (c) Naked Objects Group Ltd.
