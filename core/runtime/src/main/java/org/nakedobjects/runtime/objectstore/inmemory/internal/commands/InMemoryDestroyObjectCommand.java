package org.nakedobjects.runtime.objectstore.inmemory.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjects;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;

public final class InMemoryDestroyObjectCommand 
		extends AbstractInMemoryPersistenceCommand 
		implements DestroyObjectCommand {
	private final static Logger LOG = Logger.getLogger(InMemoryDestroyObjectCommand.class);

	public InMemoryDestroyObjectCommand(final NakedObject adapter, final ObjectStorePersistedObjects persistedObjects) {
		super(adapter, persistedObjects);
	}

	public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {
		if (LOG.isInfoEnabled()) {
			LOG.info("  delete object '" + onObject() + "'");
		}
	    destroy(onObject());
	}

	@Override
	public String toString() {
	    return "DestroyObjectCommand [object=" + onObject() + "]";
	}
}