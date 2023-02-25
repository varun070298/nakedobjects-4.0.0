package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.persistence.PersistenceSession;


/**
 * API used solely by the {@link PersistenceSession}.
 */
public interface AdapterManagerProxy extends AdapterManagerLookup {


    /**
     * Remaps an {@link Oid} that has been {@link Oid#getPrevious() updated} so that its {@link NakedObject
     * adapter} (if any) is mapped to that {@link Oid}.
     * 
     * <p>
     * Part of public API so that the proxy persistor can maintain its maps when it processes a newly
     * persisted object.
     *
     * @see AdapterManagerPersist#remapAsPersistent(NakedObject)
     */
    public void remapUpdated(Oid oid);

}

// Copyright (c) Naked Objects Group Ltd.
