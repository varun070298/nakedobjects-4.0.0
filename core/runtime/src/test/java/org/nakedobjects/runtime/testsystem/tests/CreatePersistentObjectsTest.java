package org.nakedobjects.runtime.testsystem.tests;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestPojo;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;


public class CreatePersistentObjectsTest extends ProxyJunit3TestCase {

    private NakedObject adapter;
    private TestPojo pojo;
    private TestProxyOid oid;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pojo = new TestPojo();
        adapter = system.createPersistentTestObject(pojo);
        oid = (TestProxyOid) adapter.getOid();
    }

    public void testStateOfCreatedAdapted() {
        assertNotNull(adapter);
        assertEquals(pojo, adapter.getObject());
    }

    public void testResolveState() throws Exception {
        assertEquals(ResolveState.RESOLVED, adapter.getResolveState());
    }

    public void testGivenVersion() throws Exception {
        assertEquals(new TestProxyVersion(1), adapter.getVersion());
    }

    public void testSpecification() {
        assertNotNull(adapter.getSpecification());
        assertNotNull(TestPojo.class.getName(), adapter.getSpecification().getFullName());
    }

    public void test2ndPersistentCreationHasDifferentOid() {
        final TestPojo pojo = new TestPojo();
        final NakedObject adapter2 = system.createPersistentTestObject(pojo);

        assertNotNull(adapter2);
        assertEquals(new TestProxyOid(90001, true), adapter2.getOid());
    }

    public void testOidChanged() throws Exception {
        assertEquals(new TestProxyOid(90000, true), oid);
    }

    
    public void testOidHasPrevious() throws Exception {
    	assertNotNull(oid.getPrevious());
        assertEquals(new TestProxyOid(1, false), oid.getPrevious());
    }
    
    public void testPreviousOidIsRemovedFromMap() throws Exception {
        final NakedObject a = getAdapterManager().getAdapterFor(oid.getPrevious());
        assertNull(a);
    }



    public void testIsAddedToMap() throws Exception {
        final NakedObject a = getAdapterManager().getAdapterFor(oid);
        assertEquals(adapter, a);
    }

    public void testAddedToPersistor() throws Exception {
        final NakedObject a = getPersistenceSession().loadObject(oid, adapter.getSpecification());
        assertEquals(adapter, a);
    }

    

}

// Copyright (c) Naked Objects Group Ltd.
