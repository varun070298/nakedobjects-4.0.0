package org.nakedobjects.remoting.client.facets;

import static org.hamcrest.CoreMatchers.equalTo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.remoting.client.facets.ActionInvocationFacetWrapProxy;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.testsystem.TestProxySystem;

@RunWith(JMock.class)
public class ProxyAction_ExecuteLocallyTest {

    private Mockery mockery = new JUnit4Mockery();

    private ActionInvocationFacetWrapProxy proxy;
    private NakedObjectAction mockNakedObjectAction;
    private ObjectEncoderDecoder mockEncoder;
    private ServerFacade mockDistribution;
    private NakedObject target;
    private NakedObject param1;
    
    private TestProxySystem system;
    private Identifier identifier;
    private ActionInvocationFacet mockActionInvocationFacet;

    private FacetHolder mockFacetHolder;

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.OFF);

        system = new TestProxySystem();
        system.init();

        mockNakedObjectAction = mockery.mock(NakedObjectAction.class);
        mockEncoder = mockery.mock(ObjectEncoderDecoder.class);
        mockDistribution = mockery.mock(ServerFacade.class);
        mockActionInvocationFacet = mockery.mock(ActionInvocationFacet.class);
        mockFacetHolder = mockery.mock(FacetHolder.class);
        
        identifier = Identifier.classIdentifier("");

        target = system.createTransientTestObject();
        mockery.checking(new Expectations() {
            {
                one(mockActionInvocationFacet).getFacetHolder();
                will(returnValue(mockFacetHolder));
                
                allowing(mockNakedObjectAction).getIdentifier();
                will(returnValue((identifier)));

                allowing(mockNakedObjectAction).execute(with(equalTo(target)), with(any(NakedObject[].class)));
                will(returnValue(null));
            }
        });
        
        proxy = new ActionInvocationFacetWrapProxy(mockActionInvocationFacet, mockDistribution, mockEncoder, mockNakedObjectAction);

    }

    // to prevent a warning
    @Test
    public void testDummy() {}

    @Ignore("was commented out, don't know details")
    @Test
    public void testOnTransientExecutionIsPassedToDelegate() throws Exception {
        // actionPeer.getTarget();
        // expectLastCall().andStubReturn(null);


        proxy.invoke(target, new NakedObject[] { param1, param1 });

    }

    @Ignore("was commented out, don't know details")
    @Test
    public void testOnPersistentAnnotatedAsLocalIsPassedToDelegate() throws Exception {
        // actionPeer.getTarget();
        // expectLastCall().andStubReturn(NakedObjectAction.LOCAL);

        proxy.invoke(target, new NakedObject[] { param1, param1 });
    }
}

// Copyright (c) Naked Objects Group Ltd.
