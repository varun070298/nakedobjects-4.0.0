package org.nakedobjects.plugins.xml.objectstore;

import java.util.Collections;

import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.xml.objectstore.internal.clock.TestClock;
import org.nakedobjects.plugins.xml.objectstore.internal.data.MockDataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.services.DummyServiceManager;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestSpecification;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class XmlObjectStoreTest extends ProxyJunit3TestCase {
    private XmlObjectStore objectStore;
    private MockDataManager dataManager;
    private TestProxyNakedObject nakedObject;
    private TestSpecification spec;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(XmlObjectStoreTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // system
        dataManager = new MockDataManager();
        objectStore = new XmlObjectStore(dataManager, new DummyServiceManager());
        objectStore.setClock(new TestClock());

        // objects
        spec = new TestSpecification();
        spec.fields = new NakedObjectAssociation[0];
        nakedObject = new TestProxyNakedObject();
        nakedObject.setupSpecification(spec);
        nakedObject.setOptimisticLock(new SerialNumberVersion(23, null, null));
    }

    public void testSaveObjectCreatesNewVersion() throws Exception {
        nakedObject.setOptimisticLock(null);

        CreateObjectCommand command = objectStore.createCreateObjectCommand(nakedObject);
        objectStore.execute(Collections.<PersistenceCommand>singletonList(command));

        assertEquals(new FileVersion(null, 1), nakedObject.getVersion());
    }

    public void testDeleteObjectRemovesVersion() throws Exception {
        DestroyObjectCommand command = objectStore.createDestroyObjectCommand(nakedObject);
        objectStore.execute(Collections.<PersistenceCommand>singletonList(command));

        assertEquals(null, nakedObject.getVersion());
    }

    public void testUpdateObjectCreatesNewVersion() throws Exception {
        SaveObjectCommand command = objectStore.createSaveObjectCommand(nakedObject);
        objectStore.execute(Collections.<PersistenceCommand>singletonList(command));

        assertEquals(new FileVersion(null, 1), nakedObject.getVersion());
    }
}
// Copyright (c) Naked Objects Group Ltd.
