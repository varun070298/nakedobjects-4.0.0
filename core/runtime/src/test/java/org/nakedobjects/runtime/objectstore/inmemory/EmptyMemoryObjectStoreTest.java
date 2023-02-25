package org.nakedobjects.runtime.objectstore.inmemory;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestPojo;
import org.nakedobjects.runtime.testsystem.TestProxyOid;


public class EmptyMemoryObjectStoreTest extends ProxyJunit3TestCase {
    private InMemoryObjectStore store;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new InMemoryObjectStore();
        store.open();
    }


    public void testStartsUpInUnitializedSate() throws Exception {
        assertFalse(store.isFixturesInstalled());
    }

    public void testFindNoInstances() throws Exception {
        final NakedObjectSpecification spec = system.getSpecification(TestPojo.class);
        final NakedObject[] instances = store.getInstances(new PersistenceQueryFindByTitle(spec, "title"));
        assertEquals(0, instances.length);
    }

    public void testHasNoInstances() throws Exception {
        final NakedObjectSpecification spec = system.getSpecification(TestPojo.class);
        assertFalse(store.hasInstances(spec));
    }

    public void testCantFindObjectByOid() {
        final NakedObjectSpecification spec = system.getSpecification(TestPojo.class);
        final Oid oid = new TestProxyOid(10, true);
        try {
            store.getObject(oid, spec);
            fail();
        } catch (final ObjectNotFoundException expected) {}
    }

    public void testName() throws Exception {
        assertEquals("In-Memory Object Store", store.name());

    }

    public void testOidForService() throws Exception {
        final Oid oidForService = store.getOidForService("service name");
        assertEquals(null, oidForService);
    }

}

// Copyright (c) Naked Objects Group Ltd.
