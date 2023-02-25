package org.nakedobjects.remoting.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.services.ServicesInjectorDefault;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.remoting.client.persistence.ClientSideTransactionManager;
import org.nakedobjects.remoting.client.persistence.PersistenceSessionProxy;
import org.nakedobjects.remoting.client.transaction.ClientTransactionEvent;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.DummyEncodeableObjectData;
import org.nakedobjects.remoting.data.DummyIdentityData;
import org.nakedobjects.remoting.data.DummyNullValue;
import org.nakedobjects.remoting.data.DummyObjectData;
import org.nakedobjects.remoting.data.DummyReferenceData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;
import org.nakedobjects.remoting.exchange.HasInstancesRequest;
import org.nakedobjects.remoting.exchange.KnownObjectsRequest;
import org.nakedobjects.remoting.exchange.ResolveObjectRequest;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.pojo.PojoAdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerDefault;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryBasic;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;
import org.nakedobjects.runtime.testdomain.Movie;
import org.nakedobjects.runtime.testdomain.Person;
import org.nakedobjects.runtime.testspec.MovieSpecification;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxyOidGenerator;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;

@RunWith(JMock.class)
public class ProxyPersistorTest extends ProxyJunit4TestCase {

    private Mockery mockery = new JUnit4Mockery();


    private PersistenceSessionFactory mockPersistenceSessionFactory;
    private ServerFacade mockDistribution;
    private ObjectEncoderDecoder mockEncoder;
    
    private PersistenceSessionProxy persistenceSessionProxy;
    private ClientSideTransactionManager transactionManager;
    private AuthenticationSession session;


    private AdapterManagerExtended adapterManager;


    private AdapterFactory adapterFactory;
    private ObjectFactory objectFactory;
    private OidGenerator oidGenerator;


    @Before
    public void setUp() throws Exception {
        // createSystem();


        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockDistribution = mockery.mock(ServerFacade.class);
        mockEncoder = mockery.mock(ObjectEncoderDecoder.class);
        
        adapterManager = new AdapterManagerDefault();
        adapterFactory = new PojoAdapterFactory();
        objectFactory = new ObjectFactoryBasic();
        oidGenerator = new TestProxyOidGenerator();
        
        RuntimeContextFromSession runtimeContext = new RuntimeContextFromSession();
        DomainObjectContainerDefault container = new DomainObjectContainerDefault();
        
        runtimeContext.injectInto(container);
        runtimeContext.setContainer(container);
        
        ServicesInjectorDefault servicesInjector = new ServicesInjectorDefault();
        servicesInjector.setContainer(container);
        
		persistenceSessionProxy = new PersistenceSessionProxy(
                mockPersistenceSessionFactory, 
                adapterFactory, 
                objectFactory,
                servicesInjector, 
                oidGenerator,
                adapterManager,
                mockDistribution, 
                mockEncoder);
        
        persistenceSessionProxy.setSpecificationLoader(system.getReflector());
        transactionManager = new ClientSideTransactionManager(adapterManager, persistenceSessionProxy, mockDistribution, mockEncoder);
        transactionManager.injectInto(persistenceSessionProxy);
        
        session = NakedObjectsContext.getAuthenticationSession();
        
        system.setPersistenceSession(persistenceSessionProxy);

        ignoreCallsToInitializeMocks();
        persistenceSessionProxy.open();
    }

    @After
    public void tearDown() throws Exception {}

	private static SpecificationLoader getSpecificationLoader() {
		return NakedObjectsContext.getSpecificationLoader();
	}


    @Test
    public void testClientSideMakesNoRemoteCallsWhenNoWorkNeeded() throws Exception {
        mockery.checking(new Expectations() {
            {
                never(mockDistribution);
            }
        });

        transactionManager.startTransaction();
        transactionManager.endTransaction();
    }

    @Ignore("need to get working after refactoring")
    @Test
    public void testFindInstances() throws Exception {

        // The remote interface is asked for instances, which are returned as data objects
        final DummyObjectData instanceData = new DummyObjectData(new TestProxyOid(12, true), Movie.class.getName(), true,
                new TestProxyVersion(3));

        // The data then needs to be decoded into the naked objects
        final TestProxyNakedObject dummyNakedObject = new TestProxyNakedObject();
        // new DummyOid(12, true), ResolveState.GHOST, "test");
        dummyNakedObject.setupObject(new Movie());
        dummyNakedObject.setupSpecification(new MovieSpecification());

        final PersistenceQueryData c = new TestCriteria();
        NakedObjectSpecification noSpec = getSpecificationLoader().loadSpecification(Movie.class);
		final PersistenceQueryFindAllInstances criteria = new PersistenceQueryFindAllInstances(noSpec);

		final FindInstancesRequest request = new FindInstancesRequest(session, c);
		
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).decode(instanceData);
                will(returnValue(dummyNakedObject));

				one(mockDistribution).findInstances(request);
                will(returnValue(new ObjectData[] { instanceData }));
                
                one(mockEncoder).encodePersistenceQuery(criteria);
                will(returnValue(c));
            }
        });


		final NakedObject instances = persistenceSessionProxy.findInstances(criteria);

        // the proxy should return one instance, which will be the dummy object created by the encoder's
        // restore call
        final NakedObjectList objects = (NakedObjectList) instances.getObject();
        assertEquals(1, objects.size());
        assertEquals(dummyNakedObject, objects.elements().nextElement());
    }

    @Test
    public void testResolveImmediatelyIgnoredWhenAlreadyResolving() throws Exception {

        final TestProxyNakedObject object = new TestProxyNakedObject();
        object.setupResolveState(ResolveState.RESOLVING);

        // implicit: expect no calls to encoder object
        mockery.checking(new Expectations() {
            {
                never(mockDistribution);
            }
        });

        persistenceSessionProxy.resolveImmediately(object);
    }

    @Ignore("need to get working after refactoring")
    @Test
    public void testResolveImmediately() throws Exception {

        final TestProxyNakedObject object = new TestProxyNakedObject();
        object.setupOid(new TestProxyOid(5));
        object.setupResolveState(ResolveState.GHOST);

        final IdentityData identityData = new DummyReferenceData();
        final ObjectData objectData = new DummyObjectData();
        final ResolveObjectRequest request = new ResolveObjectRequest(session, identityData);
        
        mockery.checking(new Expectations() {
            {
                // encoder used to create identity data for target object
                one(mockEncoder).encodeIdentityData(object);
                will(returnValue(identityData));
                
				// remote call asks for object's data
                one(mockDistribution).resolveImmediately(request);
                will(returnValue(objectData));
                
                // data decode expected
                one(mockEncoder).decode(objectData);
                will(returnValue(null));
            }
        });

        persistenceSessionProxy.resolveImmediately(object);

        /*
         * 
         * assertEquals("ET", movie.getName()); assertEquals(new DummyOid(5), object.getOid());
         * assertEquals(new DummyVersion(3), object.getVersion());
         */
    }

    @Ignore("TODO")
    @Test
    public void testResolveField_TBI() {}

    
    @Ignore("need to get working after refactoring")
    @Test
    public void testHasInstances() throws Exception {

    	final HasInstancesRequest request = new HasInstancesRequest(session, Movie.class.getName());
        mockery.checking(new Expectations() {
            {
				one(mockDistribution).hasInstances(request);
                will(returnValue(true));

				one(mockDistribution).hasInstances(request);
                will(returnValue(false));
            }
        });

        final NakedObjectSpecification type = getSpecificationLoader().loadSpecification(Movie.class);
        assertTrue(persistenceSessionProxy.hasInstances(type));
        assertFalse(persistenceSessionProxy.hasInstances(type));
    }

    public void testFindInstancesButNoneFound() throws Exception {

        // system.addSpecificationToLoader(new MovieSpecification());

        final PersistenceQueryData c = new TestCriteria();
        NakedObjectSpecification noSpec = getSpecificationLoader().loadSpecification(Movie.class);
		final PersistenceQueryFindAllInstances criteria = new PersistenceQueryFindAllInstances(noSpec);
		final FindInstancesRequest request = new FindInstancesRequest(session, c);
		
        mockery.checking(new Expectations() {
            {
				one(mockDistribution).findInstances(request);
                will(returnValue(new ObjectData[0]));
                
                one(mockEncoder).encodePersistenceQuery(criteria);
                will(returnValue(c));
            }
        });

		persistenceSessionProxy.findInstances(criteria);
    }

    @Test(expected=IllegalStateException.class)
    public void testClientSideActionThrowsExceptionWhenTransactionNotStarted() throws Exception {

        transactionManager.endTransaction();
    }

    @Test
    public void testClientSideActionWhereObjectDeleted() throws Exception {

        final NakedObject movieAdapter = system.createPersistentTestObject();
        
        // test starts here
        mockery.checking(new Expectations() {
            {
                final DummyIdentityData identityOfObjectToDelete = encoderShouldCreateIdentityDataForMovie(movieAdapter);
                distributionShouldExecuteClientActionForDeletedMovie(identityOfObjectToDelete);
            }

            private DummyIdentityData encoderShouldCreateIdentityDataForMovie(final NakedObject movieAdapter) {
                final DummyIdentityData identityOfObjectToDelete = new DummyIdentityData();
                
                one(mockEncoder).encodeIdentityData(movieAdapter);
                will(returnValue(identityOfObjectToDelete));
                return identityOfObjectToDelete;
            }
            private void distributionShouldExecuteClientActionForDeletedMovie(final DummyIdentityData identityOfObjectToDelete) {
                final Version[] versionUpdates = new Version[] {};
                one(mockDistribution).executeClientAction(
                        with(any(ExecuteClientActionRequest.class)));
                will(returnValue(new ExecuteClientActionResponse(new ObjectData[] {}, versionUpdates, new ObjectData[0])));
            }
        });
        
        // TODO: should look inside the request object and ensure:
        // with(equalTo(session)), 
        // with(equalTo(new ReferenceData[] { identityOfObjectToDelete })), 
        // with(equalTo(new int[] { ClientTransactionEvent.DELETE })), 

        transactionManager.startTransaction();
        persistenceSessionProxy.destroyObject(movieAdapter);
        transactionManager.endTransaction();
        final List<NakedObject> allDisposedObjects = NakedObjectsContext.getUpdateNotifier().getDisposedObjects();
        
        assertFalse(allDisposedObjects.isEmpty());
        assertEquals(movieAdapter, allDisposedObjects.get(0));
    }

    @Test
    public void testClientSideActionWhereObjectChanged() throws Exception {

        final TestProxyNakedObject directorAdapter = new TestProxyNakedObject();
        directorAdapter.setupResolveState(ResolveState.RESOLVED);

        final TestProxyNakedObject movieAdapter = new TestProxyNakedObject();
        movieAdapter.setupResolveState(ResolveState.RESOLVED);


        mockery.checking(new Expectations() {
            {
                final DummyObjectData movieData = encoderShouldCreateGraphForChangedMovie(movieAdapter);
                final DummyObjectData directorData = encoderShouldCreateGraphForChangedDirector(directorAdapter);
                distributionShouldExecuteClientActionForBothChangedObjects(movieData, directorData);
            }

            private DummyObjectData encoderShouldCreateGraphForChangedMovie(final TestProxyNakedObject movieAdapter) {
                final DummyObjectData movieData = new DummyObjectData(new TestProxyOid(12, true), Movie.class.getName(), true,
                        new TestProxyVersion(4));
                final DummyEncodeableObjectData expectedMovieName = new DummyEncodeableObjectData("War of the Worlds", String.class
                        .getName());
                final DummyReferenceData expectedDirectorRef = new DummyReferenceData(new TestProxyOid(14, true), Person.class.getName(),
                        new TestProxyVersion(8));
                movieData.setFieldContent(new Data[] { expectedDirectorRef, expectedMovieName });
                
                one(mockEncoder).encodeGraphForChangedObject(movieAdapter, new KnownObjectsRequest());
                will(returnValue(movieData));
                return movieData;
            }
            private DummyObjectData encoderShouldCreateGraphForChangedDirector(final TestProxyNakedObject directorAdapter) {
                final DummyObjectData directorData = new DummyObjectData(new TestProxyOid(14, true), Person.class.getName(), true,
                        new TestProxyVersion(8));
                final DummyEncodeableObjectData expectedDirectorName = new DummyEncodeableObjectData("Unknown", String.class.getName());
                directorData.setFieldContent(new Data[] { expectedDirectorName });
                
                one(mockEncoder).encodeGraphForChangedObject(directorAdapter, new KnownObjectsRequest());
                will(returnValue(directorData));
                return directorData;
            }

            private void distributionShouldExecuteClientActionForBothChangedObjects(
                    final DummyObjectData movieData,
                    final DummyObjectData directorData) {
                final ObjectData[] changes = new ObjectData[] { movieData, directorData };
                final int[] types = new int[] { ClientTransactionEvent.CHANGE, ClientTransactionEvent.CHANGE };
                
                one(mockDistribution).executeClientAction(
                        with(any(ExecuteClientActionRequest.class)));
                
                final Version[] versionUpdates = new Version[] { new TestProxyVersion(5), new TestProxyVersion(9) };
                will(returnValue(
                        new ExecuteClientActionResponse(
                        		new ObjectData[] { movieData, directorData }, 
                        		versionUpdates, new ObjectData[0])));
            }
        });
        // TODO: should look inside the request object and ensure:
        // with(equalTo(session)), 
        // with(equalTo(changes)), 
        // with(equalTo(types)), 


        transactionManager.startTransaction();
        persistenceSessionProxy.objectChanged(movieAdapter);
        persistenceSessionProxy.objectChanged(directorAdapter);
        transactionManager.endTransaction();


        assertEquals(new TestProxyVersion(5), movieAdapter.getVersion());
        assertEquals(new TestProxyVersion(9), directorAdapter.getVersion());
    }

    @Test
    public void testClientSideActionWhereTransientObjectMadePersistent() throws Exception {

        final NakedObject transientObject = system.createTransientTestObject();

        final TestProxyOid previousOid = (TestProxyOid) transientObject.getOid();
        final DummyObjectData movieData = new DummyObjectData(previousOid, Movie.class.getName(), true, null);
        final NullData directorData = new DummyNullValue(Person.class.getName());
        final DummyEncodeableObjectData nameData = new DummyEncodeableObjectData("Star Wars", String.class.getName());
        movieData.setFieldContent(new Data[] { directorData, nameData });

        mockery.checking(new Expectations() {
            {
                // this returns results data with new oid and version
                final TestProxyOid newOid = new TestProxyOid(123, true);
                newOid.setupPrevious(previousOid); 
                final DummyReferenceData updateData = new DummyReferenceData(newOid, "type", new TestProxyVersion(456));

                // the server is called with data (movieData) for the object to be persisted
                one(mockDistribution).executeClientAction(
                        with(any(ExecuteClientActionRequest.class)));
                
                will(returnValue(new ExecuteClientActionResponse(new ReferenceData[] { updateData }, null, new ObjectData[0])));
            }

        });
        // TODO: should look inside the request object and ensure:
        // with(equalTo(session)), 
        // with(equalTo(new ReferenceData[] { movieData })),
        // with(equalTo(new int[] { ClientTransactionEvent.ADD })), 

        getAdapterManager().adapterFor(transientObject.getObject());

        // client needs to encode the object's transient aspects
        mockery.checking(new Expectations() {
            {
                one(mockEncoder).encodeMakePersistentGraph(
                        with(equalTo(transientObject)), 
                        with(any(KnownObjectsRequest.class)));
                will(returnValue(movieData));
            }
        });

        transactionManager.startTransaction();
        persistenceSessionProxy.makePersistent(transientObject);
        transactionManager.endTransaction();
    }



    ///////////////////////////////
    // helpers
    ///////////////////////////////
    
    private void ignoreCallsToInitializeMocks() {
        mockery.checking(new Expectations() {
            {
                ignoring(mockDistribution).init();
            }
        });
    }

    private void ignoreCallsToDistribution() {
        mockery.checking(new Expectations() {
            {
                ignoring(mockDistribution);
            }
        });
    }


}

class TestCriteria implements PersistenceQueryData {
    private static final long serialVersionUID = 1L;

    public Class getPersistenceQueryClass() {
        return null;
    }

    public boolean includeSubclasses() {
        return false;
    }

    public String getType() {
        return null;
    }
}
// Copyright (c) Naked Objects Group Ltd.
