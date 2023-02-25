package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.persistence.PersistenceSessionHydrator;

/**
 * Not part of the standard API, but used by the <tt>MemoryObjectStore</tt> to add pre-existing
 * {@link NakedObject adapter}s straight into the identity maps.
 * 
 * <p>
 * Don't think this is used anymore; see instead {@link PersistenceSessionHydrator}.
 */
public interface AdapterManagerBackdoor extends AdapterManager {


    /**
     * Add a pre-existing {@link NakedObject adapter} straight into the maps.
     */
    NakedObject addExistingAdapter(NakedObject object);

}


// Copyright (c) Naked Objects Group Ltd.
