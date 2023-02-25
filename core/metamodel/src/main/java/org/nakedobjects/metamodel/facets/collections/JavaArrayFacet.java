package org.nakedobjects.metamodel.facets.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacetAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class JavaArrayFacet extends CollectionFacetAbstract {

    private final RuntimeContext runtimeContext;

    public JavaArrayFacet(final FacetHolder holder, final RuntimeContext runtimeContext) {
        super(holder);
        this.runtimeContext = runtimeContext;
    }

    /**
     * Expected to be called with a {@link NakedObject} wrapping an array.
     */
    public void init(final NakedObject nakedObjectRepresentingCollection, final NakedObject[] initData) {
        int length = initData.length;
        Object[] array = new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = initData[i].getObject();
        }
        nakedObjectRepresentingCollection.replacePojo(array);
    }

    /**
     * Expected to be called with a {@link NakedObject} wrapping an array.
     */
    public Collection<NakedObject> collection(NakedObject nakedObjectRepresentingCollection) {
        final Object[] array = array(nakedObjectRepresentingCollection);
        ArrayList<NakedObject> nakedObjectCollection = new ArrayList<NakedObject>(array.length);
        for (int i = 0; i < array.length; i++) {
            NakedObject element = getAdapterManager().getAdapterFor(array[i]);
            nakedObjectCollection.add(element);
        }
        return nakedObjectCollection;
    }

    /**
     * Expected to be called with a {@link NakedObject} wrapping an array.
     */
    public NakedObject firstElement(final NakedObject nakedObjectRepresentingCollection) {
        final Object[] array = array(nakedObjectRepresentingCollection);
        return array.length > 0 ? getAdapterManager().getAdapterFor(array[0]) : null;
    }

    /**
     * Expected to be called with a {@link NakedObject} wrapping an array.
     */
    public int size(final NakedObject nakedObjectRepresentingCollection) {
        return array(nakedObjectRepresentingCollection).length;
    }

    private Object[] array(final NakedObject nakedObjectRepresentingCollection) {
        return (Object[]) nakedObjectRepresentingCollection.getObject();
    }

    // /////////////////////////////////////////////////////
    // Dependencies (from constructor)
    // /////////////////////////////////////////////////////

    private RuntimeContext getAdapterManager() {
        return runtimeContext;
    }

}

// Copyright (c) Naked Objects Group Ltd.
