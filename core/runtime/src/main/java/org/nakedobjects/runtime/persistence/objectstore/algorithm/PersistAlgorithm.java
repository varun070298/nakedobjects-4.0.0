package org.nakedobjects.runtime.persistence.objectstore.algorithm;

import org.nakedobjects.metamodel.adapter.NakedObject;


public interface PersistAlgorithm {
    public void makePersistent(final NakedObject object, final ToPersistObjectSet adders);

    public String name();
}
// Copyright (c) Naked Objects Group Ltd.
