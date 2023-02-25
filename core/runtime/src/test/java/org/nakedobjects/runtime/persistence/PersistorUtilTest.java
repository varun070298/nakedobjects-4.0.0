package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestPojo;
import org.nakedobjects.runtime.testsystem.TestProxyOid;


public class PersistorUtilTest extends ProxyJunit3TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testRecreateTransientAdapter() {
        final Oid oid = new TestProxyOid(13, false);
        final Object object = new TestPojo();
        final NakedObject adapter = getAdapterManagerTestSupport().testCreateTransient(object, oid);
        assertEquals(oid, adapter.getOid());
        assertEquals(object, adapter.getObject());
        assertEquals(ResolveState.TRANSIENT, adapter.getResolveState());
        assertEquals(null, adapter.getVersion());
    }

    public void testRecreatePersistentAdapter() {
        final Oid oid = new TestProxyOid(15, true);
        final Object object = new TestPojo();
        final NakedObject adapter = getAdapterManagerPersist().recreateRootAdapter(oid, object);
        assertEquals(oid, adapter.getOid());
        assertEquals(object, adapter.getObject());
        assertEquals(ResolveState.GHOST, adapter.getResolveState());

        assertEquals(null, adapter.getVersion());
    }

}

// Copyright (c) Naked Objects Group Ltd.
