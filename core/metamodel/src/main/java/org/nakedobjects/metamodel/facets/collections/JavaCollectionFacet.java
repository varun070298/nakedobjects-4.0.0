package org.nakedobjects.metamodel.facets.collections;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacetAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;


public class JavaCollectionFacet extends CollectionFacetAbstract {

    private final RuntimeContext runtimeContext;

	public JavaCollectionFacet(
			final FacetHolder holder, 
			final RuntimeContext runtimeContext) {
        super(holder);
        this.runtimeContext = runtimeContext;
    }

    @SuppressWarnings("unchecked")
    public Collection<NakedObject> collection(NakedObject nakedObjectWrappingCollection) {
        Collection<?> collectionOfUnderlying = collectionOfUnderlying(nakedObjectWrappingCollection);
        return CollectionUtils.collect(collectionOfUnderlying, new ObjectToNakedObjectTransformer(getRuntimeContext()));
    }

	public NakedObject firstElement(final NakedObject collection) {
        final Iterator<NakedObject> iterator = iterator(collection);
        return iterator.hasNext() ? iterator.next() : null;
    }

    public int size(final NakedObject collection) {
        return collectionOfUnderlying(collection).size();
    }

    @SuppressWarnings("unchecked")
    public void init(final NakedObject collection, final NakedObject[] initData) {
        final Collection javaCollection = collectionOfUnderlying(collection);
        javaCollection.clear();
        for (int i = 0; i < initData.length; i++) {
            javaCollection.add(initData[i].getObject());
        }
    }

    /**
     * The underlying collection of objects (not {@link NakedObject}s).
     */
    private Collection<?> collectionOfUnderlying(final NakedObject nakedObjectWrappingCollection) {
        return (Collection<?>) nakedObjectWrappingCollection.getObject();
    }



    ////////////////////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    ////////////////////////////////////////////////////////////////////////
    
    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}


}

// Copyright (c) Naked Objects Group Ltd.
