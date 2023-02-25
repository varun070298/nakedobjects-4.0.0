package org.nakedobjects.remoting.server;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.remoting.data.DummyIdentityData;
import org.nakedobjects.remoting.data.DummyObjectData;
import org.nakedobjects.remoting.data.DummyReferenceData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.testdomain.Movie;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestProxySession;
import org.nakedobjects.runtime.testsystem.TestProxySystem;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;



@RunWith(JMock.class)
public class ServerFacadeImpl_RemoteActionTest {

    private Mockery mockery = new JUnit4Mockery();

    private AuthenticationManager mockAuthenticationManager;
    private ObjectEncoderDecoder mockEncoder;

    private ServerFacadeImpl server;
    private TestProxySystem system;
    private NakedObject adapter;
    private Oid oid;
    private DummyIdentityData targetData;
    private ReferenceData[] parameterData;
    private NakedObjectAction mockAction;

    /*
     * Testing the Distribution implementation ServerDistribution. This uses the encoder to unmarshall objects
     * and then calls the persistor and reflector; all of which should be mocked.
     */
    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.OFF);

        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockEncoder = mockery.mock(ObjectEncoderDecoder.class);

        server = new ServerFacadeImpl(mockAuthenticationManager);
        server.setEncoder(mockEncoder);

        server.init();
        

        system = new TestProxySystem();
        system.init();

        adapter = system.createPersistentTestObject();
        oid = adapter.getOid();

        targetData = new DummyIdentityData(oid, TestProxyNakedObject.class.getName(), new TestProxyVersion(1));
        parameterData = new ReferenceData[] {};
        final TestProxySpecification proxySpecification = (TestProxySpecification) adapter.getSpecification();
        
        mockAction = mockery.mock(NakedObjectAction.class);
        proxySpecification.setupAction(mockAction);
        
        mockery.checking(new Expectations() {
            {
                one(mockAction).getId();
                will(returnValue("action"));
                
                one(mockAction).execute(with(equalTo(adapter)), with(equalTo(new NakedObject[0])));
                will(returnValue(adapter));
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        system.shutdown();
    }

    @Test
    public void testExecuteOK() {
        final ExecuteServerActionResponse results = null;
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeServerActionResult(
                        with(equalTo(adapter)), 
                        with(equalTo(new ObjectData[0])),
                        with(equalTo(new ReferenceData[0])), 
                        with(nullValue(ObjectData.class)), 
                        with(equalTo(new ObjectData[0])), 
                        with(equalTo(new String[0])),
                        with(equalTo(new String[0])));
                will(returnValue(results));
                
            }
        });

        NakedObjectsContext.getTransactionManager().startTransaction();
        ExecuteServerActionRequest request = new ExecuteServerActionRequest(new TestProxySession(), 
                NakedObjectActionConstants.USER, 
                "action()", 
                targetData, 
                parameterData);
		final ExecuteServerActionResponse result = 
            server.executeServerAction(
                    request);
        NakedObjectsContext.getTransactionManager().endTransaction();

        assertEquals(results, result);
    }

    @Test
    public void testExecuteWhereObjectDeleted() {
        final ExecuteServerActionResponse results = null;
        
        mockery.checking(new Expectations() {
            {
                final ReferenceData deletedObjectIdentityData = new DummyReferenceData(adapter.getOid(), "", adapter.getVersion());
                one(mockEncoder).encodeIdentityData(adapter);
                will(returnValue(deletedObjectIdentityData));
                
                one(mockEncoder).encodeServerActionResult(
                        with(equalTo(adapter)), 
                        with(equalTo(new ObjectData[0])),
                        with(equalTo(new ReferenceData[] { deletedObjectIdentityData })), 
                        with(nullValue(ObjectData.class)), 
                        with(equalTo(new ObjectData[0])),
                        with(equalTo(new String[0])), 
                        with(equalTo(new String[0])));
                will(returnValue(null));
            }
        });

        NakedObjectsContext.getTransactionManager().startTransaction();

        NakedObjectsContext.getUpdateNotifier().addDisposedObject(adapter);
        ExecuteServerActionRequest request = new ExecuteServerActionRequest(new TestProxySession(), 
                NakedObjectActionConstants.USER, 
                "action()", 
                targetData, 
                parameterData);
		final ExecuteServerActionResponse result = server.executeServerAction(
                request );

        NakedObjectsContext.getTransactionManager().endTransaction();

        assertEquals(results, result);
    }

    @Test
    public void testExecuteWhereObjectChanged() {
        
        final ExecuteServerActionResponse results = null;
        mockery.checking(new Expectations() {
            {
                final ObjectData changedObjectData = new DummyObjectData();
                one(mockEncoder).encodeForUpdate(adapter);
                will(returnValue(changedObjectData));
                
                one(mockEncoder).encodeServerActionResult(
                        with(equalTo(adapter)), 
                        with(equalTo(new ObjectData[] { changedObjectData })),
                        with(equalTo(new ReferenceData[0] )), 
                        with(nullValue(ObjectData.class)), 
                        with(equalTo(new ObjectData[0])),
                        with(equalTo(new String[0])), 
                        with(equalTo(new String[0])));
                will(returnValue(null));
            }
        });

        NakedObjectsContext.getTransactionManager().startTransaction();

        NakedObjectsContext.getUpdateNotifier().addChangedObject(adapter);
        
        ExecuteServerActionRequest request = new ExecuteServerActionRequest(new TestProxySession(), 
                NakedObjectActionConstants.USER, 
                "action()", 
                targetData, 
                parameterData);
		final ExecuteServerActionResponse result = server.executeServerAction(
                request);

        NakedObjectsContext.getTransactionManager().endTransaction();

        assertEquals(results, result);
    }

    @Test
    public void testExecuteWhereMessagesAndWarningGenerated() {
        final ExecuteServerActionResponse results = null;
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeServerActionResult(
                        with(equalTo(adapter)), 
                        with(equalTo(new ObjectData[0])),
                        with(equalTo(new ReferenceData[0] )), 
                        with(nullValue(ObjectData.class)), 
                        with(equalTo(new ObjectData[0])),
                        with(equalTo(new String[] { "message 1", "message 2" })), 
                        with(equalTo(new String[] { "warning 1", "warning 2" })));
                will(returnValue(null));
            }
        });

        NakedObjectsContext.getTransactionManager().startTransaction();
        
        NakedObjectsContext.getMessageBroker().addMessage("message 1");
        NakedObjectsContext.getMessageBroker().addMessage("message 2");

        NakedObjectsContext.getMessageBroker().addWarning("warning 1");
        NakedObjectsContext.getMessageBroker().addWarning("warning 2");


        ExecuteServerActionRequest request = new ExecuteServerActionRequest(new TestProxySession(), 
                NakedObjectActionConstants.USER, 
                "action()", 
                targetData, 
                parameterData);
		final ExecuteServerActionResponse result = server.executeServerAction(
                request);

        NakedObjectsContext.getTransactionManager().endTransaction();

        assertEquals(results, result);
    }

}
// Copyright (c) Naked Objects Group Ltd.
