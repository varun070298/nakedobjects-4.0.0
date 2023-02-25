package org.nakedobjects.remoting.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.remoting.data.DummyReferenceData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetAssociationResponse;
import org.nakedobjects.remoting.facade.impl.ServerFacadeImpl;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyAssociation;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;


@RunWith(JMock.class)
public class ServerFacadeImpl_AssociationTest extends ProxyJunit4TestCase {

    private Mockery mockery = new JUnit4Mockery();


    private ServerFacadeImpl server;
    private AuthenticationSession authenticationSession;
    private NakedObject movieAdapter;
    private DummyReferenceData movieData;
    private DummyReferenceData personData;
    private TestProxyAssociation nameField;
    private NakedObject personAdapter;

    private AuthenticationManager mockAuthenticationManager;
    private ObjectEncoderDecoder mockObjectEncoder;

    /*
     * Testing the Distribution implementation ServerDistribution. This uses the encoder to unmarshall objects
     * and then calls the persistor and reflector; all of which should be mocked.
     */
    @Before
    public void setUp() throws Exception {

        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockObjectEncoder = mockery.mock(ObjectEncoderDecoder.class);
        
        server = new ServerFacadeImpl(mockAuthenticationManager);
        server.setEncoder(mockObjectEncoder);
        server.init();

        authenticationSession = NakedObjectsContext.getAuthenticationSession();

        movieAdapter = system.createPersistentTestObject();
        final Oid movieOid = movieAdapter.getOid();

        movieData = new DummyReferenceData(movieOid, "none", movieAdapter.getVersion());

        final TestProxySpecification spec = (TestProxySpecification) movieAdapter.getSpecification();
        nameField = new TestProxyAssociation("director", system.getSpecification(String.class));
        spec.setupFields(new NakedObjectAssociation[] { nameField });

        personAdapter = system.createPersistentTestObject();
        final Oid personOid = personAdapter.getOid();

        personData = new DummyReferenceData(personOid, "none", personAdapter.getVersion());
    }

    @After
    public void tearDown() throws Exception {
        system.shutdown();
    }

    @Test
    public void testClearAssociation() {
        /*
         * other tests for clear: - clear collection element - fails if unauthorised - fails if unavailable
         * 
         * could place all these clear test in one class; test other methods in other classes
         */
        NakedObjectsContext.getTransactionManager().startTransaction();
        ClearAssociationRequest request = new ClearAssociationRequest(authenticationSession, "director", movieData, personData);
		ClearAssociationResponse response = server.clearAssociation(request);
		final ObjectData[] updatesData = response.getUpdates();
        NakedObjectsContext.getTransactionManager().endTransaction();

        nameField.assertFieldEmpty(movieAdapter);
        assertEquals(0, updatesData.length);
    }

    @Test
    public void testSetAssociation() {
        NakedObjectsContext.getTransactionManager().startTransaction();
        SetAssociationRequest request = new SetAssociationRequest(authenticationSession, "director", movieData, personData);
        SetAssociationResponse response = server.setAssociation(request);
		final ObjectData[] updates = response.getUpdates();
        NakedObjectsContext.getTransactionManager().endTransaction();

        nameField.assertField(movieAdapter, personAdapter.getObject());
        assertEquals(0, updates.length);
    }

    @Test
    public void testSetAssociationFailsWithNonCurrentTarget() {
        // version should be different, causing concurrency exception
        movieAdapter.setOptimisticLock(new TestProxyVersion(6));
        try {
        	SetAssociationRequest request = new SetAssociationRequest(authenticationSession, "director", movieData, personData);
            server.setAssociation(request);
            fail();
        } catch (final ConcurrencyException expected) {}
    }

    @Test
    public void testSetAssociationFailsWhenInvisible() {
        nameField.setUpIsVisible(false);
        try {
            SetAssociationRequest request = new SetAssociationRequest(authenticationSession, "director", movieData, personData);
			server.setAssociation(request);
            fail();
        } catch (final NakedObjectException expected) {
            assertEquals("can't modify field as not visible or editable", expected.getMessage());
        }
    }

    @Test
    public void testSetAssociationFailsWhenUnavailable() {
        nameField.setUpIsUnusableFor(movieAdapter);
        try {
        	SetAssociationRequest request = new SetAssociationRequest(authenticationSession, "director", movieData, personData);
            server.setAssociation(request);
            fail();
        } catch (final NakedObjectException expected) {
            assertEquals("can't modify field as not visible or editable", expected.getMessage());
        }
    }

    @Test
    public void testSetAssociationFailsWithNonCurrentAssociate() {
        // version should be different, causing concurrency exception
        personAdapter.setOptimisticLock(new TestProxyVersion(6));
        try {
        	SetAssociationRequest request = new SetAssociationRequest(authenticationSession, "director", movieData, personData);
            server.setAssociation(request);
            fail();
        } catch (final ConcurrencyException expected) {}
    }

}
// Copyright (c) Naked Objects Group Ltd.
