package org.nakedobjects.runtime.persistence.adaptermanager;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;



public abstract class AdapterManagerAbstract implements AdapterManagerExtended  {


    public void removeAdapter(Oid oid) {
        NakedObject adapter = getAdapterFor(oid);
        if (adapter != null) {
        	removeAdapter(adapter);
        }
    }

    public void injectInto(Object candidate) {
        if (AdapterManagerAware.class.isAssignableFrom(candidate.getClass())) {
            AdapterManagerAware cast = AdapterManagerAware.class.cast(candidate);
            cast.setAdapterManager(this);
        }
    }


}

// Copyright (c) Naked Objects Group Ltd.
