package org.nakedobjects.runtime.persistence.objectstore;

import java.util.List;

import org.nakedobjects.runtime.persistence.objectstore.transaction.ObjectStoreTransaction;
import org.nakedobjects.runtime.persistence.objectstore.transaction.ObjectStoreTransactionManager;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


/**
 * Interface for the {@link NakedObjectTransactionManager} to interact with the
 * {@link ObjectStore}.
 */
public interface ObjectStoreTransactionManagement {

    /**
     * Used by the {@link ObjectStoreTransactionManager} to tell the underlying
     * {@link ObjectStore} to start a transaction.
     */
    void startTransaction();

    /**
     * Used by the current {@link ObjectStoreTransaction} to flush changes to
     * the {@link ObjectStore} (either via a {@link NakedObjectTransactionManager#flushTransaction()}
     * or a {@link NakedObjectTransactionManager#endTransaction()}).
     */
    void execute(List<PersistenceCommand> unmodifiableList);

    /**
     * Used by the {@link ObjectStoreTransactionManager} to tell the underlying
     * {@link ObjectStore} to commit a transaction.
     */
    void endTransaction();

    /**
     * Used by the {@link ObjectStoreTransactionManager} to tell the underlying
     * {@link ObjectStore} to abort a transaction.
     */
    void abortTransaction();


}


// Copyright (c) Naked Objects Group Ltd.
