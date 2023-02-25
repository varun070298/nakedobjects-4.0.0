package org.nakedobjects.runtime.testsystem;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetDefaultToObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.authorization.standard.AuthorizationManagerStandard;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.imageloader.TemplateImageLoaderNoop;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerPersist;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerTestSupport;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.session.NakedObjectSessionDefault;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifierDefault;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;
import org.nakedobjects.runtime.userprofile.UserProfileLoaderDefault;
import org.nakedobjects.runtime.userprofile.UserProfileStore;
import org.nakedobjects.runtime.userprofile.UserProfileLoaderDefault.Mode;
import org.nakedobjects.runtime.userprofile.inmemory.InMemoryUserProfileStore;


//TODO combine with all versions of TestSystem
public class TestProxySystem {
    
    private int nextId = 1;
    private final TestProxyConfiguration configuration;
    private NakedObjectsContext context;
    
    private UserProfileLoader userProfileLoader;
    private UserProfileStore userprofileStore;
    private TestProxyPersistenceSessionFactory persistenceSessionFactory;
    
    private PersistenceSession persistenceSession;
    private final TestProxyReflector reflector;
    private final UpdateNotifierDefault updateNotifier;
    private final TemplateImageLoader noopTemplateImageLoader;
    protected AuthenticationManager authenticationManager;
    protected AuthorizationManager authorizationManager;
	private List<Object> servicesList;


    public TestProxySystem() {
        noopTemplateImageLoader = new TemplateImageLoaderNoop();
        reflector = new TestProxyReflector();
        
        servicesList = Collections.emptyList();

        // all a bit hacky...
        persistenceSessionFactory = new TestProxyPersistenceSessionFactory();
        userprofileStore = new InMemoryUserProfileStore();
        userProfileLoader = new UserProfileLoaderDefault(userprofileStore, Mode.RELAXED);
        persistenceSession = new TestProxyPersistenceSession(persistenceSessionFactory);
        persistenceSessionFactory.setPersistenceSessionToCreate(persistenceSession);
        
        configuration = new TestProxyConfiguration();
        configuration.add(ConfigurationConstants.ROOT + "locale", "en_GB");
        authenticationManager = new AuthenticationManagerNoop();
        authorizationManager = new AuthorizationManagerNoop();
        updateNotifier = new UpdateNotifierDefault();
    }
    

    public TestProxyNakedObject createAdapterForTransient(final Object associate) {
        final TestProxyNakedObject testProxyNakedObject = new TestProxyNakedObject();
        testProxyNakedObject.setupObject(associate);
        testProxyNakedObject.setupSpecification(getSpecification(associate.getClass()));
        testProxyNakedObject.setupResolveState(ResolveState.TRANSIENT);
        testProxyNakedObject.setupOid(new TestProxyOid(nextId++));
        return testProxyNakedObject;
    }

    public void init() {
    	reflector.setRuntimeContext(new RuntimeContextFromSession());
    	
        NakedObjectSessionFactory sessionFactory = 
            new NakedObjectSessionFactoryDefault(
                    DeploymentType.EXPLORATION, 
                    configuration, 
                    noopTemplateImageLoader, 
                    reflector, 
                    authenticationManager, 
                    authorizationManager,
                    userProfileLoader, 
                    persistenceSessionFactory, servicesList);
        
        persistenceSession.setSpecificationLoader(reflector);
        // this implementation of persistenceSession will automatically inject 
        // its own transaction manager into itself.
        
        sessionFactory.init();
        context = NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
        
        // commented out cos think now redundant since calling openExecutionContext below
        // persistor.open();

        NakedObjectsContext.openSession(new TestProxySession());

    }

    public void shutdown() {
        NakedObjectsContext.closeAllSessions();
    }

    public void resetLoader() {
        persistenceSession.testReset();
    }

    public NakedObject createPersistentTestObject() {
        final TestPojo pojo = new TestPojo();
        return createPersistentTestObject(pojo);
    }

    public NakedObject createPersistentTestObject(final Object domainObject) {
        final NakedObject adapter = createTransientTestObject(domainObject);
        
        // similar to object store implementation
        getAdapterManagerPersist().remapAsPersistent(adapter);
        
        // would be done by the object store, we must do ourselves.
        adapter.setOptimisticLock(new TestProxyVersion(1));
        
        return adapter;
    }

    public void makePersistent(final TestProxyNakedObject adapter) {
        final Oid oid = adapter.getOid();
        getOidGenerator().convertTransientToPersistentOid(oid);
        adapter.setupOid(oid);
        persistenceSession.makePersistent(adapter);
    }

    // commented out since never used locally
//    private void setUpSpecification(final TestPojo pojo, final TestProxyNakedObject adapter) {
//        adapter.setupSpecification(reflector.loadSpecification(pojo.getClass()));
//    }

    // commented out since never used locally
//    private void addAdapterToIdentityMap(final Object domainObject, final NakedObject adapter) {
//        ((PersistenceSessionSpy) persistor).addAdapter(domainObject, adapter);
//    }

    public NakedObject createTransientTestObject() {
        final TestPojo pojo = new TestPojo();
        return createTransientTestObject(pojo);
    }

    public NakedObject createTransientTestObject(final Object domainObject) {
        final TestProxyOid oid = new TestProxyOid(nextId++, false);
        final NakedObject adapterFor = getAdapterManagerTestSupport().testCreateTransient(domainObject, oid);
        Assert.assertEquals("", ResolveState.TRANSIENT, adapterFor.getResolveState());
        return adapterFor;
    }

    public TestProxySpecification getSpecification(final Class<?> type) {
        return (TestProxySpecification) reflector.loadSpecification(type);
    }

    public void setPersistenceSession(final PersistenceSession persistor) {
        this.persistenceSession = persistor;
        if (context != null) {
            NakedObjectSessionDefault current = (NakedObjectSessionDefault) context.getSessionInstance();
            current.testSetObjectPersistor(persistor);
        }
    }

    /**
     * 
     */
    public TestProxyNakedCollection createPersistentTestCollection() {
        final TestProxyNakedCollection collection = new TestProxyNakedCollection(new Vector());
        final TestProxySpecification specification = getSpecification(Vector.class);
        final TestProxySpecification elementSpecification = getSpecification(Object.class);
        specification.addFacet(new TestProxyCollectionFacet());
        specification.addFacet(new TypeOfFacetDefaultToObject(elementSpecification, reflector) {});
        collection.setupSpecification(specification);
        return collection;
    }

    public TestProxySpecification getSpecification(final NakedObject object) {
        return (TestProxySpecification) object.getSpecification();
    }

    public void addSpecification(final NakedObjectSpecification specification) {
        reflector.addSpecification(specification);
    }

    public void addConfiguration(final String name, final String value) {
        configuration.add(name, value);
    }

    public UpdateNotifier getUpdateNotifer() {
        return updateNotifier;
    }

    public NakedObjectReflector getReflector() {
        return reflector;
    }

    public PersistenceSession getPersistenceSession() {
        return persistenceSession;
    }
    
    public TestProxyConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Created automatically by the persistor.
     */
    public AdapterFactory getAdapterFactory() {
        return  persistenceSession.getAdapterFactory();
    }
    
    /**
     * Created automatically by the persistor.
     */
    public AdapterManager getAdapterManager() {
        return persistenceSession.getAdapterManager();
    }

    public AdapterManagerTestSupport getAdapterManagerTestSupport() {
        return (AdapterManagerTestSupport) persistenceSession.getAdapterManager();
    }

    public AdapterManagerPersist getAdapterManagerPersist() {
        return (AdapterManagerPersist) persistenceSession.getAdapterManager();
    }


    private NakedObjectTransactionManager getTransactionManager() {
        return persistenceSession.getTransactionManager();
    }


    private OidGenerator getOidGenerator() {
        return persistenceSession.getOidGenerator();
    }


}
// Copyright (c) Naked Objects Group Ltd.
