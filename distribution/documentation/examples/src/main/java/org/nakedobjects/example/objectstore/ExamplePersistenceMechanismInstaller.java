package org.nakedobjects.example.objectstore;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStorePersistenceMechanismInstallerAbstract;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator;
import org.nakedobjects.runtime.system.DeploymentType;


public class ExamplePersistenceMechanismInstaller extends ObjectStorePersistenceMechanismInstallerAbstract {

	private ExampleObjectStore objectStore;
	
    public ExamplePersistenceMechanismInstaller() {
		super("example");
	}

    @Override
    protected ObjectStore createObjectStore(
            NakedObjectConfiguration configuration,
            AdapterFactory nakedObjectFactory,
            AdapterManager adapterManager) {
        if (objectStore == null) {
            objectStore = new ExampleObjectStore(configuration);
        }
        return objectStore;
    }

    @Override
    protected OidGenerator createOidGenerator(NakedObjectConfiguration configuration) {
        return new SimpleOidGenerator();
    }

    public PersistenceSessionFactory createPersistenceSessionFactory(final DeploymentType deploymentType) {
        return new PersistenceSessionFactoryDelegating(deploymentType, this) {};
    }
}
// Copyright (c) Naked Objects Group Ltd.
