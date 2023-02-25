package org.nakedobjects.runtime.objectstore.inmemory.internal;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator.Memento;

/**
 * Represents the persisted objects.
 * 
 * Attached and detached to each session.
 */
public interface ObjectStorePersistedObjects {

    public Memento getOidGeneratorMemento();
    public void saveOidGeneratorMemento(Memento memento);

    public void registerService(String name, Oid oid);
    public Oid getService(String name);

	public Iterable<NakedObjectSpecification> specifications();
	public ObjectStoreInstances instancesFor(NakedObjectSpecification spec);
	public Iterable<ObjectStoreInstances> instances();

	public void clear();
	

}


// Copyright (c) Naked Objects Group Ltd.
