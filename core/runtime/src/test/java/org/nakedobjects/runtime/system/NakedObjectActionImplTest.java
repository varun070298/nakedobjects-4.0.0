package org.nakedobjects.runtime.system;

import junit.framework.TestSuite;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacetAbstract;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.internal.NakedObjectActionImpl;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestSpecification;


@RunWith(JMock.class)
public class NakedObjectActionImplTest extends ProxyJunit3TestCase {
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(NakedObjectActionImplTest.class));
    }

    private final Mockery mockery = new JUnit4Mockery();

    private NakedObjectActionImpl action;
    private NakedObjectActionPeer mockNakedObjectActionPeer;
    private RuntimeContext mockRuntimeContext;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        mockNakedObjectActionPeer = mockery.mock(NakedObjectActionPeer.class);
        mockRuntimeContext = mockery.mock(RuntimeContext.class);

        action = new NakedObjectActionImpl("reduceheadcount", mockNakedObjectActionPeer, mockRuntimeContext);
    }

    @Test
    public void testExecutePassedOnToPeer() {
        final TestProxyNakedObject target = new TestProxyNakedObject();
        target.setupSpecification(new TestSpecification());
        final NakedObject[] parameters = new NakedObject[2];

        final TestProxyNakedObject result = new TestProxyNakedObject();
        final ActionInvocationFacet facet = new ActionInvocationFacetAbstract(mockNakedObjectActionPeer) {
            public NakedObject invoke(NakedObject target, NakedObject[] parameters) {
                return result;
            }

            public NakedObjectSpecification getReturnType() {
                return null;
            }

            public NakedObjectSpecification getOnType() {
                return new TestSpecification();
            }
        };

        mockery.checking(new Expectations() {
            {
                exactly(2).of(mockNakedObjectActionPeer).getFacet(ActionInvocationFacet.class);
                will(returnValue(facet));
            }
        });

        final NakedObject returnObject = action.execute(target, parameters);
        assertEquals(returnObject, result);
    }

    @Test
    public void testNameDefaultsToActionsMethodName() {
        final NamedFacet facet = new NamedFacetAbstract("Reduceheadcount", mockNakedObjectActionPeer) {};
        mockery.checking(new Expectations() {
            {
                one(mockNakedObjectActionPeer).getFacet(NamedFacet.class);
                will(returnValue(facet));
            }
        });
        assertEquals("Reduceheadcount", action.getName());
    }

    @Test
    public void testId() {
        assertEquals("reduceheadcount", action.getId());
    }

}
// Copyright (c) Naked Objects Group Ltd.
