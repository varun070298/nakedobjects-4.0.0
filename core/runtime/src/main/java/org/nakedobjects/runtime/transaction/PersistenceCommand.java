package org.nakedobjects.runtime.transaction;

import org.nakedobjects.metamodel.adapter.NakedObject;


public interface PersistenceCommand {
    void execute(NakedObjectTransaction transaction);

    NakedObject onObject();
}
// Copyright (c) Naked Objects Group Ltd.
