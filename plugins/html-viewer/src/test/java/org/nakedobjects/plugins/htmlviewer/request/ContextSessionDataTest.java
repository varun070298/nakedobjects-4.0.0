package org.nakedobjects.plugins.htmlviewer.request;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestPojo;


public class ContextSessionDataTest extends ProxyJunit3TestCase {

    private NakedObject originalAdapter;
    private Oid oid;
    private NakedObject restoredAdapter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        originalAdapter = system.createTransientTestObject(new TestPojo());
        oid = originalAdapter.getOid();

        final Context context = new Context(null);
        context.mapObject(originalAdapter);

        assertNotNull("loader still has the object", getAdapterManager().getAdapterFor(oid));
        system.resetLoader();
        assertNull("loader no longer has the object", getAdapterManager().getAdapterFor(oid));

        context.restoreAllObjectsToLoader();
        restoredAdapter = getAdapterManager().getAdapterFor(oid);
    }


    public void testExistsInLoader() {
        assertNotNull("loaders is missing the object", getAdapterManager().getAdapterFor(oid));
        assertNotSame("expect the loader to have a new adapter", originalAdapter, restoredAdapter);
    }

    public void testHasSameOid() {
        assertEquals(originalAdapter.getOid(), restoredAdapter.getOid());
    }

    public void testNotSameAdapter() {
        assertNotSame(originalAdapter, restoredAdapter);
    }

    public void testSamePojo() {
        assertEquals(originalAdapter.getObject().getClass(), restoredAdapter.getObject().getClass());
    }

    public void testHasSameVersion() {
        assertEquals(originalAdapter.getVersion(), restoredAdapter.getVersion());
    }

    public void testHasResolveStateOfTransient() {
        assertEquals(ResolveState.TRANSIENT, restoredAdapter.getResolveState());
    }

}

// Copyright (c) Naked Objects Group Ltd.
