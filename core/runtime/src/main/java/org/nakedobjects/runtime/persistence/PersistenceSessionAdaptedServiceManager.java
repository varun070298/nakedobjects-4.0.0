package org.nakedobjects.runtime.persistence;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;

public interface PersistenceSessionAdaptedServiceManager {
	
    NakedObject getService(String id);

    List<NakedObject> getServices();    
}

// Copyright (c) Naked Objects Group Ltd.
