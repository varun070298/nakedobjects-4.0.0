package org.nakedobjects.runtime.objectstore.inmemory.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjects;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;

public final class InMemorySaveObjectCommand 
		extends AbstractInMemoryPersistenceCommand 
		implements SaveObjectCommand {
	
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InMemorySaveObjectCommand.class);

	public InMemorySaveObjectCommand(NakedObject object, final ObjectStorePersistedObjects persistedObjects) {
		super(object, persistedObjects);
	}

	public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {
	    save(onObject());
	}

	@Override
	public String toString() {
	    return "SaveObjectCommand [object=" + onObject() + "]";
	}
}