package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.component.Injectable;


/**
 * Responsible for managing the {@link NakedObject adapter}s and {@link Oid identities} for each and every
 * POJO that is being used by the framework.
 * 
 * <p>
 * It provides a consistent set of adapters in memory, providing an {@link NakedObject adapter} for the POJOs
 * that are in use ensuring that the same object is not loaded twice into memory.
 * 
 * <p>
 * Each POJO is given an {@link NakedObject adapter} so that the framework can work with the POJOs even though
 * it does not understand their types. Each POJO maps to an {@link NakedObject adapter} and these are reused.
 */
public interface AdapterManagerLookup extends Injectable {

    
    /**
     * Gets the {@link NakedObject adapter} for the specified domain object if it exists in the identity map.
     * 
     * @param pojo - must not be <tt>null</tt>
     * @return adapter, or <tt>null</tt> if doesn't exist.
     */
    NakedObject getAdapterFor(Object pojo);

    /**
     * Gets the {@link NakedObject adapter} for the {@link Oid} if it exists in the identity map.
     * 
     * @param oid - must not be <tt>null</tt>
     * @return adapter, or <tt>null</tt> if doesn't exist.
     */
    NakedObject getAdapterFor(Oid oid);

    
}

// Copyright (c) Naked Objects Group Ltd.
