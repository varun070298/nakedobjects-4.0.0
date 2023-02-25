package org.nakedobjects.runtime.objectstore.inmemory;

import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStoreInstances;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjects;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjectsDefault;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegate;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.nakedobjects.runtime.persistence.objectstore.PersistenceSessionObjectStore;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator;
import org.nakedobjects.runtime.system.DeploymentType;


public class InMemoryPersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    private ObjectStorePersistedObjects persistedObjects;

    public InMemoryPersistenceSessionFactory(
            final DeploymentType deploymentType,
            final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        super(deploymentType, persistenceSessionFactoryDelegate);
    }

    protected ObjectStorePersistedObjects getPersistedObjects() {
		return persistedObjects;
	}

    @Override
    public PersistenceSession createPersistenceSession() {
        PersistenceSessionObjectStore persistenceSession = (PersistenceSessionObjectStore) super.createPersistenceSession();
        InMemoryObjectStore inMemoryObjectStore = (InMemoryObjectStore) persistenceSession.getObjectStore();
        if (persistedObjects != null) {
            OidGenerator oidGenerator = persistenceSession.getOidGenerator();
            if (oidGenerator instanceof SimpleOidGenerator) {
                SimpleOidGenerator simpleOidGenerator = (SimpleOidGenerator) oidGenerator;
                simpleOidGenerator.resetTo(persistedObjects.getOidGeneratorMemento());
            }
            inMemoryObjectStore.attachPersistedObjects(persistedObjects);
            persistedObjects = null;
        }

        return persistenceSession;
    }


    /**
     * Not API - called when {@link InMemoryObjectStore} first {@link InMemoryObjectStore#open() open}ed.
     */
	public ObjectStorePersistedObjects createPersistedObjects() {
		return new ObjectStorePersistedObjectsDefault();
	}

    /**
     * Not API - called when {@link InMemoryObjectStore} is {@link InMemoryObjectStore#close() close}d.
     */
    public void attach(final PersistenceSession persistenceSession, final ObjectStorePersistedObjects persistedObjects) {
        OidGenerator oidGenerator = persistenceSession.getOidGenerator();
        if (oidGenerator instanceof SimpleOidGenerator) {
            SimpleOidGenerator simpleOidGenerator = (SimpleOidGenerator) oidGenerator;
            persistedObjects.saveOidGeneratorMemento(simpleOidGenerator.getMemento());
        }
        this.persistedObjects = persistedObjects;
    }

    
    @Override
    protected void doShutdown() {
        if (persistedObjects != null) {
        	for (ObjectStoreInstances inst: persistedObjects.instances()) {
        		inst.shutdown();
        	}
        	persistedObjects.clear();
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
