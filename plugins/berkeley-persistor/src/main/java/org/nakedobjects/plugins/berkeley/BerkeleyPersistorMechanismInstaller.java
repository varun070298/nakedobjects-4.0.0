package org.nakedobjects.plugins.berkeley;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.system.DeploymentType;


public class BerkeleyPersistorMechanismInstaller extends ObjectStorePersistenceMechanismInstallerAbstract {
	
	private BerkeleyObjectStore objectStore;

	public BerkeleyPersistorMechanismInstaller() {
		super("berkeley");
	}
	
    @Override
    protected ObjectStore createObjectStore(NakedObjectConfiguration configuration, AdapterFactory nakedObjectFactory, AdapterManager adapterManager) {
        return getObjectStore(configuration);
    }

    private BerkeleyObjectStore getObjectStore(NakedObjectConfiguration configuration) {
        if (objectStore == null) {
            objectStore = new BerkeleyObjectStore(configuration);
        }
        return objectStore;
    }

    @Override
    protected OidGenerator createOidGenerator(NakedObjectConfiguration configuration) {
        return getObjectStore(configuration).getOidGenerator();
     }

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new PersistenceSessionFactoryDelegating(deploymentType, this) {};
    }
}
// Copyright (c) Naked Objects Group Ltd.
