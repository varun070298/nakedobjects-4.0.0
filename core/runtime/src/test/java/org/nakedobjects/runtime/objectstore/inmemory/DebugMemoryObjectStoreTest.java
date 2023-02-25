package org.nakedobjects.runtime.objectstore.inmemory;

import java.util.Collections;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class DebugMemoryObjectStoreTest extends ProxyJunit3TestCase {
    private InMemoryObjectStore store;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new InMemoryObjectStore();
        store.open();
    }


    public void testObject() throws Exception {
        final NakedObject object = system.createPersistentTestObject();

        final CreateObjectCommand command = store.createCreateObjectCommand(object);
        store.execute(Collections.<PersistenceCommand>singletonList(command));

        store.debugTitle();
        final DebugString debug = new DebugString();
        store.debugData(debug);
    }

    public void testEmpty() throws Exception {
        store.debugTitle();
        final DebugString debug = new DebugString();
        store.debugData(debug);
    }
}

// Copyright (c) Naked Objects Group Ltd.
