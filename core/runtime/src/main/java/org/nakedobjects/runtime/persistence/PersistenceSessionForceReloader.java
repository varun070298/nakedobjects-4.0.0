package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.adapter.NakedObject;

public interface PersistenceSessionForceReloader {

    /**
     * Forces a reload of this object from the persistent object store.
     */
    void reload(NakedObject object);
}

// Copyright (c) Naked Objects Group Ltd.
