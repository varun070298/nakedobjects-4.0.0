package org.nakedobjects.runtime.objectstore.inmemory.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStoreInstances;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjects;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.runtime.transaction.PersistenceCommandAbstract;

public abstract class AbstractInMemoryPersistenceCommand extends PersistenceCommandAbstract {

	private final static Logger LOG = Logger.getLogger(AbstractInMemoryPersistenceCommand.class);

	private final ObjectStorePersistedObjects persistedObjects;
	
	public AbstractInMemoryPersistenceCommand(final NakedObject adapter, final ObjectStorePersistedObjects persistedObjects) {
		super(adapter);
		this.persistedObjects = persistedObjects;
	}

	protected void save(final NakedObject adapter) throws ObjectPersistenceException {
        final NakedObjectSpecification specification = adapter.getSpecification();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("   saving object " + adapter + " as instance of " + specification.getShortName());
        }
        final ObjectStoreInstances ins = instancesFor(specification);
        ins.save(adapter); // also sets the version
    }

    protected void destroy(final NakedObject adapter) {
        final NakedObjectSpecification specification = adapter.getSpecification();
        if (LOG.isDebugEnabled()) {
        	LOG.debug("   destroy object " + adapter + " as instance of " + specification.getShortName());
        }
        final ObjectStoreInstances ins = instancesFor(specification);
        ins.remove(adapter.getOid());
    }
    
    private ObjectStoreInstances instancesFor(final NakedObjectSpecification spec) {
    	return persistedObjects.instancesFor(spec);
    }
}