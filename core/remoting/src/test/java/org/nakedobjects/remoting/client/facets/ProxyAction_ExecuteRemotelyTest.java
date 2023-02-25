package org.nakedobjects.remoting.client.facets;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer;
import org.nakedobjects.remoting.client.facets.ActionInvocationFacetWrapProxy;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.DummyNullValue;
import org.nakedobjects.remoting.data.DummyReferenceData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.testsystem.TestProxySystem;

public class ProxyAction_ExecuteRemotelyTest {

    private Mockery mockery = new JUnit4Mockery();

    private ActionInvocationFacetWrapProxy proxy;
    private NakedObjectActionPeer mockNakedObjectActionPeer;
    private ObjectEncoderDecoder mockEncoder;
    private ServerFacade mockDistribution;
    private NakedObject target;
    private NakedObject param1;
    private TestProxySystem system;
    private Identifier identifier;
    private ActionInvocationFacet mockActionInvocationFacet;
    private ReferenceData targetData;

    private Data[] parameterData;
    private NakedObject[] parameters;
    private String identifierString;

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.OFF);

//        system = new TestProxySystem();
//        system.init();
//
//        mockNakedObjectActionPeer = mockery.mock(NakedObjectActionPeer.class);
//        mockEncoder = mockery.mock(ObjectEncoder.class);
//        mockDistribution = mockery.mock(Distribution.class);
//        mockActionInvocationFacet = mockery.mock(ActionInvocationFacet.class);
//
//        identifier = Identifier.propertyOrCollectionIdentifier("A", "b");
//        
//        mockNakedObjectActionPeer.getIdentifier();
//        expectLastCall().andStubReturn(identifier);
//        identifierString = identifier.getClassName() + "#" + identifier.getMemberName();
//        target = system.createTransientTestObject();
//        parameters = new NakedObject[] { param1, param1 };
//        final NakedObjectSpecification[] parameterTypes = new NakedObjectSpecification[] {
//                system.getSpecification(TestPojo.class), system.getSpecification(TestPojo.class) };
//
//        // actionPeer.getParameterTypes();
//        // expectLastCall().andStubReturn(parameterTypes);
//
//        // actionPeer.getType();
//        // expectLastCall().andReturn(NakedObjectAction.USER);
//
//        final KnownObjects encodersKnownObjects = new KnownObjects();
//
//        targetData = new DummyReferenceData();
//        parameterData = new Data[] { null, null };
//        mockEncoder.createParameters(parameterTypes, parameters, encodersKnownObjects);
//        expectLastCall().andReturn(parameterData);
//
//        mockEncoder.createActionTarget(target, encodersKnownObjects);
//        expectLastCall().andReturn(targetData);
//
//        mockEncoder.madePersistent(null, null);
//        expectLastCall().times(2);

        
    }

    // to prevent a warning
    @Test
    public void testDummy() {}

    @Ignore("was commented out, don't know details")
    @Test
    public void testOnTransientObjectWithRemoteAnnotation() throws Exception {
        // actionPeer.getTarget();
        // expectLastCall().andStubReturn(NakedObjectAction.REMOTE);

        ExecuteServerActionRequest request = 
        	new ExecuteServerActionRequest(
        		NakedObjectsContext.getAuthenticationSession(), 
        		NakedObjectActionConstants.USER,
                identifierString, targetData, parameterData);
		mockDistribution.executeServerAction(request );
        final ExecuteServerActionResponse result = new ExecuteServerActionResponse(new DummyNullValue("type"), new ObjectData[0],
                new ReferenceData[0], null, new ObjectData[2], new String[0], new String[0]);
        expectLastCall().andReturn(result);

        mockEncoder.madePersistent(target, null);
        expectLastCall();

        replay(mockNakedObjectActionPeer, mockEncoder, mockDistribution);
        proxy.invoke(target, parameters);
        verify(mockNakedObjectActionPeer, mockEncoder, mockDistribution);
    }

    @Ignore("was commented out, don't know details")
    @Test
    public void testOnPersistent() throws Exception {
        // actionPeer.getTarget();
        // expectLastCall().andStubReturn(null);

        NakedObjectsContext.getPersistenceSession().makePersistent(target);

        ExecuteServerActionRequest request = new ExecuteServerActionRequest(
        		NakedObjectsContext.getAuthenticationSession(), 
        		NakedObjectActionConstants.USER,
                identifierString, targetData, parameterData);
		mockDistribution.executeServerAction(request);
        final ExecuteServerActionResponse result = new ExecuteServerActionResponse(new DummyNullValue("type"), new ObjectData[0],
                new ReferenceData[0], null, new ObjectData[2], new String[0], new String[0]);
        expectLastCall().andReturn(result);

        replay(mockNakedObjectActionPeer, mockEncoder, mockDistribution);
        proxy.invoke(target, parameters);
        verify(mockNakedObjectActionPeer, mockEncoder, mockDistribution);
    }

    @Ignore("was commented out, don't know details")
    @Test
    public void testObjectsDestroyed() throws Exception {
        // actionPeer.getTarget();
        // expectLastCall().andStubReturn(null);

        NakedObjectsContext.getPersistenceSession().makePersistent(target);

        final NakedObject object = system.createPersistentTestObject();

        ExecuteServerActionRequest request = new ExecuteServerActionRequest(
        		NakedObjectsContext.getAuthenticationSession(), 
        		NakedObjectActionConstants.USER,
                identifierString, targetData, parameterData);
		mockDistribution.executeServerAction(request);
        final ReferenceData[] disposedReferenceData = new ReferenceData[] { new DummyReferenceData(object.getOid(), object
                .getSpecification().getFullName(), null) };
        final ExecuteServerActionResponse result = new ExecuteServerActionResponse(new DummyNullValue("type"), new ObjectData[0],
                disposedReferenceData, null, new ObjectData[2], new String[0], new String[0]);
        expectLastCall().andReturn(result);

        replay(mockNakedObjectActionPeer, mockEncoder, mockDistribution);
        proxy.invoke(target, parameters);
        verify(mockNakedObjectActionPeer, mockEncoder, mockDistribution);

        final List<NakedObject> allDisposedObjects = NakedObjectsContext.getUpdateNotifier().getDisposedObjects();
        assertEquals(false, allDisposedObjects.isEmpty());
        assertEquals(object, allDisposedObjects.get(0));
        assertEquals(true, allDisposedObjects.isEmpty());
    }
}

// Copyright (c) Naked Objects Group Ltd.
