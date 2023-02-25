package org.nakedobjects.runtime.objectstore.inmemory;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.system.DeploymentType;


/**
 * Installs the in-memory object store.
 */
public class InMemoryPersistenceMechanismInstaller extends ObjectStorePersistenceMechanismInstallerAbstract  {
    
    

    public InMemoryPersistenceMechanismInstaller() {
		super("in-memory");
	}

    
    /////////////////////////////////////////////////////////////////
    // createPersistenceSessionFactory
    /////////////////////////////////////////////////////////////////

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new InMemoryPersistenceSessionFactory(deploymentType, this);
    }

    
    /////////////////////////////////////////////////////////////////
    // Hook methods
    /////////////////////////////////////////////////////////////////

    /**
     * Hook method to return {@link ObjectStore}. 
     */
    protected ObjectStore createObjectStore(
            final NakedObjectConfiguration configuration, 
            final AdapterFactory adapterFactory, 
            final AdapterManager adapterManager) {
        return new InMemoryObjectStore();
    }





}
// Copyright (c) Naked Objects Group Ltd.
