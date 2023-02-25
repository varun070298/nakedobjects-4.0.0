package org.nakedobjects.runtime.persistence.adapterfactory;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;

public abstract class AdapterFactoryAbstract implements AdapterFactory {

    public abstract NakedObject createAdapter(Object pojo, Oid oid);

    /**
     * Default implementation does nothing.
     */
    public void open() {}

    /**
     * Default implementation does nothing.
     */
    public void close() {}
    
    /**
     * Injects.
     */
    public void injectInto(Object candidate) {
        if (AdapterFactoryAware.class.isAssignableFrom(candidate.getClass())) {
            AdapterFactoryAware cast = AdapterFactoryAware.class.cast(candidate);
            cast.setAdapterFactory(this);
        }
    }

}


// Copyright (c) Naked Objects Group Ltd.
