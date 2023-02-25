package org.nakedobjects.runtime.objectstore.inmemory;

import java.util.Collections;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class MemoryObjectStoreTest extends ProxyJunit3TestCase {
    private InMemoryObjectStore store;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new InMemoryObjectStore();
        system.getPersistenceSession().injectInto(store);
        store.open();
    }

    @Override
    protected void tearDown() throws Exception {
        store.close();
        super.tearDown();
    }

    public void testObjectNotPersistedWhenCreated() throws Exception {
        final NakedObject object = system.createPersistentTestObject();

        final NakedObjectSpecification specification = object.getSpecification();
        assertEquals(false, store.hasInstances(specification));
        assertEquals(0, store.getInstances(new PersistenceQueryFindAllInstances(specification)).length);
        try {
            store.getObject(object.getOid(), specification);
            fail();
        } catch (final ObjectNotFoundException expected) {}
    }

    public void testRetrievedInstanceAdapterIsIsolatedFromOriginal() throws Exception {
        final NakedObject object = system.createPersistentTestObject();
        addObjectToStore(object);
        resetIdentityMap();

        final NakedObjectSpecification specification = object.getSpecification();
        final NakedObject[] retrievedInstance = store.getInstances(new PersistenceQueryFindAllInstances(specification));
        assertEquals(1, retrievedInstance.length);
        assertSame(object.getObject(), retrievedInstance[0].getObject());
        assertNotSame(object, retrievedInstance[0]);
    }

    public void testRetrievedObjectAdapterIsIsolatedFromOriginal() throws Exception {
        final NakedObject object = system.createPersistentTestObject();

        addObjectToStore(object);
        resetIdentityMap();

        final NakedObjectSpecification specification = object.getSpecification();
        final NakedObject retrievedObject = store.getObject(object.getOid(), specification);
        assertNotSame(object, retrievedObject);
        assertEquals(object.getObject(), retrievedObject.getObject());
    }

    public void testResetClearsAdapterFromLoader() throws Exception {
        final NakedObject object = system.createPersistentTestObject();
        addObjectToStore(object);
        resetIdentityMap();

        assertNull(getAdapterManager().getAdapterFor(object.getObject()));
    }

    public void testHasInstances() throws Exception {
        final NakedObject object = system.createPersistentTestObject();
        addObjectToStore(object);
        resetIdentityMap();

        final NakedObjectSpecification specification = object.getSpecification();
        assertEquals(true, store.hasInstances(specification));
    }

    public void testRetrievedInstancesByTitle() throws Exception {
        final NakedObject object = system.createPersistentTestObject();
        // object.setupTitleString("title string");
        addObjectToStore(object);
        resetIdentityMap();

        final NakedObjectSpecification specification = object.getSpecification();
        final NakedObject[] retrievedInstance = store.getInstances(new PersistenceQueryFindByTitle(specification, "le STR"));
        assertEquals(1, retrievedInstance.length);
        assertNotSame(object, retrievedInstance[0]);
        assertSame(object.getObject(), retrievedInstance[0].getObject());
    }

    public void testOidForService() throws Exception {
        final TestProxyOid oid = new TestProxyOid(14);
        store.registerService("service name", oid);
        resetIdentityMap();

        final Oid oidForService = store.getOidForService("service name");
        assertEquals(oid, oidForService);
    }

    private void resetIdentityMap() {
        NakedObjectsContext.getPersistenceSession().testReset();
    }

    public void testCantRegisterServiceMoreThanOnce() throws Exception {
        TestProxyOid oid = new TestProxyOid(14);
        store.registerService("service name", oid);
        oid = new TestProxyOid(15);
        try {
            store.registerService("service name", oid);
            fail();
        } catch (final NakedObjectException expected) {}
    }

    public void testRemoveInstance() throws Exception {
        final NakedObject object = system.createPersistentTestObject();
        addObjectToStore(object);
        resetIdentityMap();

        final PersistenceCommand command = store.createDestroyObjectCommand(object);
        assertEquals(object, command.onObject());
        store.execute(Collections.<PersistenceCommand>singletonList(command));

        resetIdentityMap();

        final NakedObjectSpecification specification = object.getSpecification();
        assertEquals(false, store.hasInstances(specification));
    }

    public void testSaveInstance() throws Exception {
        final NakedObject object = system.createPersistentTestObject();
        addObjectToStore(object);
        resetIdentityMap();

        final NakedObjectSpecification specification = object.getSpecification();
        NakedObject[] retrievedInstance = store.getInstances(new PersistenceQueryFindByTitle(specification, "changed"));
        assertEquals(0, retrievedInstance.length);

        ((TestProxyNakedObject) object).setupTitleString("changed title");
        final PersistenceCommand command = store.createSaveObjectCommand(object);
        assertEquals(object, command.onObject());
        store.execute(Collections.<PersistenceCommand>singletonList(command));

        resetIdentityMap();

        retrievedInstance = store.getInstances(new PersistenceQueryFindByTitle(specification, "changed"));
        assertEquals(1, retrievedInstance.length);
        assertNotSame(object, retrievedInstance[0]);
    }

    private void addObjectToStore(final NakedObject object) {
        final PersistenceCommand command = store.createCreateObjectCommand(object);
        assertEquals(object, command.onObject());
        store.execute(Collections.<PersistenceCommand>singletonList(command));
    }

}

// Copyright (c) Naked Objects Group Ltd.
