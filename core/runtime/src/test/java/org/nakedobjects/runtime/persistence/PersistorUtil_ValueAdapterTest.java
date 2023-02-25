package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.AggregatedOid;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedFacetAlways;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyField;


public class PersistorUtil_ValueAdapterTest extends ProxyJunit3TestCase {

    private NakedObject aggregatedAdapter;
    private NakedObject parent;
    private TestProxyField field;
    private Object value;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        parent = system.createPersistentTestObject();
        field = new TestProxyField("fieldName", system.getSpecification(Object.class));
        FacetUtil.addFacet(new AggregatedFacetAlways(field));
        value = new Object();
        aggregatedAdapter = getAdapterManager().adapterFor(value, parent, field);
    }

    public void testOidKnowsParent() throws Exception {
        final AggregatedOid aggregatedOid = (AggregatedOid) aggregatedAdapter.getOid();
        assertEquals(parent.getOid(), aggregatedOid.getParentOid());
    }

    public void testOidKnowsField() throws Exception {
        final AggregatedOid aggregatedOid = (AggregatedOid) aggregatedAdapter.getOid();
        assertEquals("fieldName", aggregatedOid.getFieldName());
    }

    public void testResolveStateStartsAsGhost() throws Exception {
        assertEquals(ResolveState.GHOST, aggregatedAdapter.getResolveState());
    }

    public void testSameParametersRetrievesSameAdapter() throws Exception {
        final NakedObject valueAdapter2 = getAdapterManager().adapterFor(value, parent, field);
        assertSame(aggregatedAdapter, valueAdapter2);

    }

}

// Copyright (c) Naked Objects Group Ltd.
