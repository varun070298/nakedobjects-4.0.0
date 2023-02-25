package org.nakedobjects.remoting.server;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.remoting.client.transaction.ClientTransactionEvent;
import org.nakedobjects.remoting.data.DummyIdentityData;
import org.nakedobjects.remoting.data.DummyObjectData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestProxySession;
import org.nakedobjects.runtime.testsystem.TestProxySystem;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;


@RunWith(JMock.class)
public class ServerFacadeImpl_ClientActionTest  {


    private Mockery mockery = new JUnit4Mockery();

    private AuthenticationManager mockAuthenticationManager;
    private ObjectEncoderDecoder mockEncoder;

    private ServerFacadeImpl server;
    private AuthenticationSession session;
    private TestProxySystem system;

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

        session = NakedObjectsContext.getAuthenticationSession();
    }

    @After
    public void tearDown() throws Exception {
        system.shutdown();
    }

    @Test
    public void testExecuteClientActionWithNoWork() {
        
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeClientActionResult(
                        with(equalTo(new ObjectData[0])), 
                        with(equalTo(new Version[0])), 
                        with(equalTo(new ObjectData[0])));
                will(returnValue(new ExecuteClientActionResponse(new ObjectData[0], new Version[0], null)));
            }
        });


        ExecuteClientActionRequest request = new ExecuteClientActionRequest(session, new ReferenceData[0], new int[0]);
        
		// don't start xactn here, since within call.
        final ExecuteClientActionResponse result = server.executeClientAction(request);

        assertEquals(0, result.getPersisted().length);
        assertEquals(0, result.getChanged().length);
    }

    @Test
    public void testExecuteClientActionWhereObjectChanged() {
        final NakedObject adapter = system.createPersistentTestObject();

        final DummyObjectData data = new DummyObjectData(adapter.getOid(), "none", true, new TestProxyVersion(1));

        // prepare the update data to return
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).decode(data, new KnownObjectsRequest());
                will(returnValue(adapter));
                
            }
        });

        // results returned in their own container
        final ExecuteClientActionResponse results = new ExecuteClientActionResponse(new ObjectData[0], new Version[0], null);
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeClientActionResult(
                        with(equalTo(new ReferenceData[1])), 
                        with(equalTo(new Version[] { new TestProxyVersion(2) })),
                        with(equalTo(new ObjectData[0])));
                will(returnValue(results));
            }
        });

        ExecuteClientActionRequest request = 
        	new ExecuteClientActionRequest(session, new ReferenceData[] { data }, new int[] { ClientTransactionEvent.CHANGE });
		// don't start xactn here, since within call.
        final ExecuteClientActionResponse result = 
            server.executeClientAction(
                    request);
        final NakedObject object = 
            NakedObjectsContext.getPersistenceSession().loadObject(adapter.getOid(), adapter.getSpecification());
        

        assertEquals(new TestProxyVersion(2), object.getVersion());

        assertEquals(results, result);
    }


    @Test
    public void testExecuteClientActionWhereObjectMadePersistent() {
        final NakedObject adapter = system.createTransientTestObject();

        final DummyObjectData data = new DummyObjectData(adapter.getOid(), "none", true, new TestProxyVersion(1));

        // restore the object on the server
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).decode(data, new KnownObjectsRequest());
                will(returnValue(adapter));
                
                one(mockEncoder).encodeIdentityData(adapter);
                will(returnValue(null));
            }
        });

        // return results
        final ExecuteClientActionResponse results = new ExecuteClientActionResponse(new ObjectData[0], new Version[0], new ObjectData[0]);
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeClientActionResult(
                        with(equalTo(new ReferenceData[1])), 
                        with(equalTo(new Version[1])), 
                        with(equalTo(new ObjectData[0])));
                will(returnValue(results));
            }
        });
        
        // don't start xactn here, since within call.

        ExecuteClientActionRequest request = new ExecuteClientActionRequest(session, new ReferenceData[] { data }, new int[] { ClientTransactionEvent.ADD });
		final ExecuteClientActionResponse response = 
            server.executeClientAction(
                    request);

        final NakedObject object = NakedObjectsContext.getPersistenceSession().loadObject(adapter.getOid(),
                adapter.getSpecification());
        

        assertEquals(results, response);
        assertEquals(adapter, object);
        assertEquals(new TestProxyVersion(1), object.getVersion());
    }

    @Test
    public void testExecuteClientActionFailsWithConcurrencyError() {
        final NakedObject adapter = system.createPersistentTestObject();
        adapter.setOptimisticLock(new TestProxyVersion(7));

        final Oid oid = adapter.getOid();
        final DummyIdentityData identityData = new DummyIdentityData(oid, TestProxyNakedObject.class.getName(),
                new TestProxyVersion(6));

        try {
            ExecuteClientActionRequest request = new ExecuteClientActionRequest(
            		new TestProxySession(), new ReferenceData[] { identityData }, new int[] { ClientTransactionEvent.DELETE });
			server.executeClientAction(request);
            fail();
        } catch (final ConcurrencyException expected) {}
    }

    @Test
    public void testExecuteClientActionWhereObjectDeleted() {
        final NakedObject adapter = system.createPersistentTestObject();

        final Oid oid = adapter.getOid();
        final DummyIdentityData identityData = new DummyIdentityData(oid, TestProxyNakedObject.class.getName(),
                new TestProxyVersion(1));

        // return results
        final ExecuteClientActionResponse results = new ExecuteClientActionResponse(new ObjectData[0], new Version[0], null);
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeClientActionResult(
                        with(equalTo(new ReferenceData[1])), 
                        with(equalTo(new Version[1])), 
                        with(equalTo(new ObjectData[0])));
                will(returnValue(results));
            }
        });

        // don't start xactn here, since within call.
        ExecuteClientActionRequest request = 
        	new ExecuteClientActionRequest(new TestProxySession(), new ReferenceData[] { identityData }, new int[] { ClientTransactionEvent.DELETE });
		final ExecuteClientActionResponse result = 
            server.executeClientAction(
                    request);

        assertEquals(results, result);
    }

}
// Copyright (c) Naked Objects Group Ltd.
