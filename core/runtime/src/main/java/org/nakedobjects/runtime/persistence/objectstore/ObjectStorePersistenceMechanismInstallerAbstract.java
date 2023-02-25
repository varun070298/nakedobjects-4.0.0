package org.nakedobjects.runtime.persistence.objectstore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.nakedobjects.metamodel.commons.ensure.Ensure.ensureThatArg;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.PersistenceSessionTransactionManagement;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.dflt.DefaultPersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.transaction.ObjectStoreTransactionManager;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;


public abstract class ObjectStorePersistenceMechanismInstallerAbstract extends PersistenceMechanismInstallerAbstract {

	public ObjectStorePersistenceMechanismInstallerAbstract(String name) {
		super(name);
	}
	
    /**
     * Will return a {@link PersistenceSessionObjectStore}; subclasses are free to downcast if required.
     */
    protected PersistenceSession createPersistenceSession(
            final PersistenceSessionFactory persistenceSessionFactory,
            final AdapterManagerExtended adapterManager,
            final AdapterFactory adapterFactory,
            final ObjectFactory objectFactory,
            final OidGenerator oidGenerator,
            final ServicesInjector servicesInjector) {

        final PersistAlgorithm persistAlgorithm = createPersistAlgorithm(getConfiguration());
        final ObjectStore objectStore = createObjectStore(getConfiguration(), adapterFactory, adapterManager);

        ensureThatArg(persistAlgorithm, is(not(nullValue())));
        ensureThatArg(objectStore, is(not(nullValue())));

        final PersistenceSessionObjectStore persistenceSession = createObjectStorePersistor(persistenceSessionFactory,
                adapterFactory, objectFactory, servicesInjector, oidGenerator, adapterManager, persistAlgorithm, objectStore);

        NakedObjectTransactionManager transactionManager = createTransactionManager(persistenceSession, objectStore);

        ensureThatArg(persistenceSession, is(not(nullValue())));
        ensureThatArg(transactionManager, is(not(nullValue())));

        persistenceSession.setDirtiableSupport(true);
        transactionManager.injectInto(persistenceSession);

        // ... and finally return
        return persistenceSession;
    }

    // ///////////////////////////////////////////
    // Optional hook methods
    // ///////////////////////////////////////////

    /**
     * Can optionally be overridden, but by default creates an {@link PersistenceSessionObjectStore}.
     */
    protected PersistenceSessionObjectStore createObjectStorePersistor(
            PersistenceSessionFactory persistenceSessionFactory,
            final AdapterFactory adapterFactory,
            final ObjectFactory objectFactory,
            final ServicesInjector servicesInjector,
            final OidGenerator oidGenerator,
            final AdapterManagerExtended adapterManager,
            final PersistAlgorithm persistAlgorithm,
            final ObjectStorePersistence objectStore) {
        return new PersistenceSessionObjectStore(persistenceSessionFactory, adapterFactory, objectFactory, servicesInjector,
                oidGenerator, adapterManager, persistAlgorithm, objectStore);
    }

    /**
     * Hook method to create {@link PersistAlgorithm}.
     * 
     * <p>
     * By default returns a {@link DefaultPersistAlgorithm}.
     */
    protected PersistAlgorithm createPersistAlgorithm(NakedObjectConfiguration configuration) {
        return new DefaultPersistAlgorithm();
    }

    /**
     * Hook method to return an {@link NakedObjectTransactionManager}.
     * 
     * <p>
     * By default returns a {@link ObjectStoreTransactionManager}.
     */
    protected NakedObjectTransactionManager createTransactionManager(
            final PersistenceSessionTransactionManagement persistor,
            final ObjectStoreTransactionManagement objectStore) {
        return new ObjectStoreTransactionManager(persistor, objectStore);
    }

    // ///////////////////////////////////////////
    // Mandatory hook methods
    // ///////////////////////////////////////////

    /**
     * Hook method to return {@link ObjectStore}.
     */
    protected abstract ObjectStore createObjectStore(
            NakedObjectConfiguration configuration,
            AdapterFactory adapterFactory,
            AdapterManager adapterManager);

}

// Copyright (c) Naked Objects Group Ltd.
