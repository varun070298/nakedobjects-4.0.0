package org.nakedobjects.runtime.objectstore.inmemory.internal;

import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator.Memento;

/**
 * Represents the persisted objects.
 * 
 * Attached and detached to each session.
 */
public class ObjectStorePersistedObjectsDefault implements ObjectStorePersistedObjects {

    private final Map<NakedObjectSpecification, ObjectStoreInstances> instancesBySpecMap;
    private final Map<String, Oid> serviceOidByIdMap;

	private Memento oidGeneratorMemento;

    public ObjectStorePersistedObjectsDefault() {
        instancesBySpecMap = new HashMap<NakedObjectSpecification, ObjectStoreInstances>();
        serviceOidByIdMap = new HashMap<String, Oid>();
    }

    
    public Memento getOidGeneratorMemento() {
		return oidGeneratorMemento;
	}
	public void saveOidGeneratorMemento(Memento memento) {
		this.oidGeneratorMemento = memento;
	}

	public Oid getService(String name) {
		return serviceOidByIdMap.get(name);
	}
	public void registerService(String name, Oid oid) {
    	Oid oidLookedUpByName = serviceOidByIdMap.get(name);
    	if (oidLookedUpByName != null) {
    		if (!oidLookedUpByName.equals(oid)) {
    			throw new NakedObjectException(
    					"Already another service registered as name: " + name + 
    					" (existing Oid: " + oidLookedUpByName + ", " +
    					"intended: " + oid + ")");
    		}
    	} else {
    		serviceOidByIdMap.put(name, oid);
    	}
	}




	// TODO: this is where the clever logic needs to go to determine how to save into our custom Map.
	// also think we shouldn't surface the entire Map, just the API we require (keySet, values etc).
	public ObjectStoreInstances instancesFor(NakedObjectSpecification spec) {
        ObjectStoreInstances ins = instancesBySpecMap.get(spec);
        if (ins == null) {
            ins = new ObjectStoreInstances(spec);
            instancesBySpecMap.put(spec, ins);
        }
        return ins;
	}


	public Iterable<NakedObjectSpecification> specifications() {
		return instancesBySpecMap.keySet();
	}

	public void clear() {
		instancesBySpecMap.clear();		
	}

	public Iterable<ObjectStoreInstances> instances() {
		return instancesBySpecMap.values();
	}


}


// Copyright (c) Naked Objects Group Ltd.
