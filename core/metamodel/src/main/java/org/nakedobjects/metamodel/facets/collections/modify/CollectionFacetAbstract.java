package org.nakedobjects.metamodel.facets.collections.modify;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;


public abstract class CollectionFacetAbstract extends FacetAbstract implements CollectionFacet {

    public CollectionFacetAbstract(final FacetHolder holder) {
        super(CollectionFacet.class, holder, false);
    }
    
    @SuppressWarnings("unchecked")
    public final Enumeration<NakedObject> elements(final NakedObject nakedObjectRepresentingCollection) {
        return new IteratorEnumeration(iterator(nakedObjectRepresentingCollection));
    }

    public final boolean contains(final NakedObject nakedObjectRepresentingCollection, final NakedObject candidateNakedObject) {
        Collection<NakedObject> collection = collection(nakedObjectRepresentingCollection);
        return collection.contains(candidateNakedObject);
    }

    @SuppressWarnings("unchecked")
    public final Iterator<NakedObject> iterator(final NakedObject nakedObjectRepresentingCollection) {
        Collection<NakedObject> collection = collection(nakedObjectRepresentingCollection);
        return collection.iterator();
    }

    public Iterable<NakedObject> iterable(final NakedObject nakedObjectRepresentingCollection) {
        return new Iterable<NakedObject>() {
            public Iterator<NakedObject> iterator() {
                return CollectionFacetAbstract.this.iterator(nakedObjectRepresentingCollection);
            }
        };
    }

    public final TypeOfFacet getTypeOfFacet() {
        return getFacetHolder().getFacet(TypeOfFacet.class);
    }


}

// Copyright (c) Naked Objects Group Ltd.
