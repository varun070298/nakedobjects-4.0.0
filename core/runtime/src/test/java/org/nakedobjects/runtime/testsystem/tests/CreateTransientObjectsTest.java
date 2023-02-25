package org.nakedobjects.runtime.testsystem.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.testsystem.TestPojo;
import org.nakedobjects.runtime.testsystem.TestProxyException;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxySystem;



public class CreateTransientObjectsTest {

    private TestProxySystem system;
    private NakedObject adapter;
    private TestPojo pojo;
    private Oid oid;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        system = new TestProxySystem();
        system.init();

        pojo = new TestPojo();
        adapter = system.createTransientTestObject(pojo);
        oid = adapter.getOid();
    }

    @Test
    public void testSpecification() {
        assertNotNull(adapter.getSpecification());
        assertNotNull(TestPojo.class.getName(), adapter.getSpecification().getFullName());
    }

    @Test
    public void testStateOfCreatedAdapted() {
        assertNotNull(adapter);
        assertEquals(pojo, adapter.getObject());
    }

    @Test
    public void testResolveStateShowsTransient() throws Exception {
        assertEquals(ResolveState.TRANSIENT, adapter.getResolveState());
    }

    @Test
    public void testOid() throws Exception {
        assertEquals(new TestProxyOid(1, false), oid);
    }

    @Test
    public void test2ndPersistentCreationHasDifferentOid() {
        final TestPojo pojo = new TestPojo();
        final NakedObject adapter2 = system.createTransientTestObject(pojo);

        assertNotNull(adapter2);
        assertEquals(new TestProxyOid(2, false), adapter2.getOid());
    }

    @Test
    public void testIsAddedToObjectLoader() {
        final NakedObject a = getAdapterManager().getAdapterFor(oid);
        assertEquals(adapter, a);
    }

    
    @Test
    public void testAddedToPersistor() {
        system.resetLoader();
        try {
            getPersistenceSession().loadObject(oid, adapter.getSpecification());
            fail();
        } catch (final TestProxyException expected) {}
    }

    @Test
    public void testNotGivenVersion() throws Exception {
        assertNull(adapter.getVersion());
    }

    @Test
    public void testOidHasNoPrevious() throws Exception {
        assertNull(oid.getPrevious());
    }
    

    
    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }
    
}

// Copyright (c) Naked Objects Group Ltd.
