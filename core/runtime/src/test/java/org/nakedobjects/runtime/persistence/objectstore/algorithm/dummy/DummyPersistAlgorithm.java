package org.nakedobjects.runtime.persistence.objectstore.algorithm.dummy;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.ToPersistObjectSet;


public class DummyPersistAlgorithm implements PersistAlgorithm {

    public void makePersistent(final NakedObject object, final ToPersistObjectSet adder) {
        adder.addPersistedObject(object);
    }

    public String name() {
        return null;
    }

    public void init() {}

    public void shutdown() {}

}
// Copyright (c) Naked Objects Group Ltd.
