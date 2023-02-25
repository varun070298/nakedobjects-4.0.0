package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.SpecificationLoaderAware;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManagerAware;


public interface PersistenceSession extends
        PersistenceSessionContainer,
        PersistenceSessionForceReloader, 
        PersistenceSessionAdaptedServiceManager, 
        PersistenceSessionTransactionManagement, 
        PersistenceSessionHydrator, 
        PersistenceSessionTestSupport, 
        SpecificationLoaderAware,
        NakedObjectTransactionManagerAware,
        SessionScopedComponent,
        Injectable,
        DebugInfo {

    


	/**
     * The {@link PersistenceSessionFactory} that created this {@link PersistenceSession}.
     */
    public PersistenceSessionFactory getPersistenceSessionFactory();
    
    // ///////////////////////////////////////////////////////////////////////////
    // open, close
    // ///////////////////////////////////////////////////////////////////////////

    public void open();
    
    public void close();


    /**
     * Determine if the object store has been initialized with its set of start up objects.
     * 
     * <p>
     * This method is called only once after the {@link ApplicationScopedComponent#init()} has been called. If this flag
     * returns <code>false</code> the framework will run the fixtures to initialise the persistor.
     */
    boolean isFixturesInstalled();

    


    
    
    // ///////////////////////////////////////////////////////////////////////////
    // Dependencies
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * The configured {@link OidGenerator}.
     */
    OidGenerator getOidGenerator();

    /**
     * The configured {@link AdapterFactory}.
     * 
     * @return
     */
    AdapterFactory getAdapterFactory();
    
    /**
     * The configured {@link ObjectFactory}.
     */
	public ObjectFactory getObjectFactory();

    /**
     * The configured {@link ServicesInjector}.
     */
    ServicesInjector getServicesInjector();


    /**
     * The configured {@link AdapterManager}.
     */
    AdapterManager getAdapterManager();
    


    /**
     * Inject the {@link NakedObjectTransactionManager}.
     * 
     * <p>
     * This must be injected using setter-based injection rather than through the constructor
     * because there is a bidirectional relationship between the {@link PersistenceSessionHydrator}
     * and the {@link NakedObjectTransactionManager}.
     * 
     * @see #getTransactionManager()
     */
    void setTransactionManager(final NakedObjectTransactionManager transactionManager);

    /**
     * The configured {@link NakedObjectTransactionManager}.
     * 
     * @see #setTransactionManager(NakedObjectTransactionManager)
     */
    NakedObjectTransactionManager getTransactionManager();

    
    /**
     * Inject the {@link SpecificationLoader}.
     * 
     * <p>
     * The need to inject the reflector was introduced to support the HibernateObjectStore, which installs
     * its own <tt>HibernateClassStrategy</tt> to cope with the proxy classes that Hibernate wraps around
     * lists, sets and maps.
     */
    void setSpecificationLoader(SpecificationLoader specificationLoader);





}
// Copyright (c) Naked Objects Group Ltd.
