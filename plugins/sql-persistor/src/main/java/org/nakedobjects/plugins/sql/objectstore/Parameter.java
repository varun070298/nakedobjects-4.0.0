package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public interface Parameter {
    void setupParameter(int parameter, StoredProcedure procedure) throws ObjectPersistenceException;

    // String getRestoreString();

    void retrieve(int parameter, StoredProcedure procedure) throws ObjectPersistenceException;
}
// Copyright (c) Naked Objects Group Ltd.
