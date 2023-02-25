package org.nakedobjects.runtime.persistence.objectstore.algorithm;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.persistence.PersistenceSession;


/**
 * Set of {@link NakedObject}s that require persisting.
 * 
 * <p>
 * Is consumed by {@link PersistAlgorithm}, and is ultimately implemented by {@link PersistenceSession}.
 */
public interface ToPersistObjectSet {

    void addPersistedObject(NakedObject object);

    void remapAsPersistent(final NakedObject object);

}
// Copyright (c) Naked Objects Group Ltd.
