package org.nakedobjects.runtime.testsystem;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGeneratorAbstract;


public class TestProxyOidGenerator extends OidGeneratorAbstract {
    private int transientId = 1;
    private int persistentId = 90000;

    
    private TestProxyOid createOid() {
        return new TestProxyOid(transientId++);
    }

    public String name() {
        return "";
    }

    public TestProxyOid createTransientOid(final Object object) {
        return createOid();
    }

    public void convertTransientToPersistentOid(final Oid oid) {
    	TestProxyOid testProxyOid = (TestProxyOid) oid;
    	testProxyOid.setNewId(persistentId++);
        testProxyOid.makePersistent();
    }

    public void debugData(final DebugString debug) {}

    public String debugTitle() {
        return null;
    }



}
// Copyright (c) Naked Objects Group Ltd.
