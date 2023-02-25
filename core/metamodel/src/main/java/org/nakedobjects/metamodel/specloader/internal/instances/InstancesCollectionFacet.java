package org.nakedobjects.metamodel.specloader.internal.instances;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacetAbstract;


public class InstancesCollectionFacet extends CollectionFacetAbstract {

    public InstancesCollectionFacet(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Expected to be called with a {@link NakedObject} wrapping a {@link NakedObjectList}.
     */
    public List<NakedObject> collection(final NakedObject nakedObjectWrappingNakedObjectList) {
        return (NakedObjectList) nakedObjectWrappingNakedObjectList.getObject();
    }

    
    /**
     * Expected to be called with a {@link NakedObject} wrapping a {@link NakedObjectList}.
     */
    public NakedObject firstElement(final NakedObject nakedObjectWrappingInstanceCollectionVector) {
        List<NakedObject> icv = collection(nakedObjectWrappingInstanceCollectionVector);
        return icv.size() > 0? icv.get(0): null;
    }

    /**
     * Expected to be called with a {@link NakedObject} wrapping a {@link NakedObjectList}.
     */
    public int size(final NakedObject nakedObjectWrappingInstanceCollectionVector) {
        return collection(nakedObjectWrappingInstanceCollectionVector).size();
    }

    /**
     * Does nothing.
     */
    public void init(final NakedObject collection, final NakedObject[] initData) {}


}

// Copyright (c) Naked Objects Group Ltd.
