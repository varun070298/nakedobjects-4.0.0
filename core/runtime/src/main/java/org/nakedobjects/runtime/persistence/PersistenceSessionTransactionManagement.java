package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.spec.Dirtiable;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;

/**
 * As called by the {@link NakedObjectTransactionManager}.
 * 
 * <p>
 * Dirtiable support.
 */
public interface PersistenceSessionTransactionManagement { 

    /**
     * Mark as {@link #objectChanged(NakedObject) changed } all {@link Dirtiable} 
     * objects that have been {@link Dirtiable#markDirty(NakedObject) manually marked}
     * as dirty.
     * 
     * <p>
     * Called by the {@link NakedObjectTransactionManager}.
     */
    void objectChangedAllDirty();

    void clearAllDirty();

    NakedObject reload(Oid oid);
     
}