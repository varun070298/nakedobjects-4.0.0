package org.nakedobjects.runtime.persistence.adapterfactory.pojo;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestPojo;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;


public class PojoAdapterTest extends ProxyJunit3TestCase {

    private NakedObject nakedObject;
    private TestPojo domainObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        domainObject = new TestPojo();
        nakedObject = new PojoAdapter(domainObject, new TestProxyOid(1));
        nakedObject.setOptimisticLock(new TestProxyVersion());
    }

    public void testOid() {
        assertEquals(new TestProxyOid(1), nakedObject.getOid());
    }

    public void testObject() {
        assertEquals(domainObject, nakedObject.getObject());
    }

    public void testInitialResolvedState() {
        assertEquals(ResolveState.NEW, nakedObject.getResolveState());
    }

    public void testChangeResolvedState() {
        nakedObject.changeState(ResolveState.TRANSIENT);
        assertEquals(ResolveState.TRANSIENT, nakedObject.getResolveState());
    }

    public void testVersion() throws Exception {
        assertEquals(new TestProxyVersion(), nakedObject.getVersion());
    }

    public void testVersionConflict() throws Exception {
        try {
            nakedObject.checkLock(new TestProxyVersion(2));
            fail();
        } catch (final ConcurrencyException expected) {}
    }
}
// Copyright (c) Naked Objects Group Ltd.
