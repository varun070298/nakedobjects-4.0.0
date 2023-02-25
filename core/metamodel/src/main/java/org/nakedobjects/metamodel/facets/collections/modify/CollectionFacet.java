package org.nakedobjects.metamodel.facets.collections.modify;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Attached to {@link NakedObjectSpecification}s that represent a 
 * collection.
 * 
 * <p>
 * Factories of (implementations of this) facet should ensure that a
 * {@link TypeOfFacet} is also attached to the same facet holder.  The
 * {@link #getTypeOfFacet()} is a convenience for this.
 */
public interface CollectionFacet extends Facet {

    int size(NakedObject collection);

    /**
     * @deprecated - use {@link #iterator(NakedObject)}
     */
    @Deprecated
    Enumeration<NakedObject> elements(NakedObject nakedObjectRepresentingCollection);

    Iterable<NakedObject> iterable(NakedObject nakedObjectRepresentingCollection);

    Iterator<NakedObject> iterator(NakedObject nakedObjectRepresentingCollection);
    
    /**
     * Returns an unmodifiable {@link Collection} of {@link NakedObject}s. 
     */
    Collection<NakedObject> collection(NakedObject nakedObjectRepresentingCollection);

    NakedObject firstElement(NakedObject nakedObjectRepresentingCollection);

    boolean contains(NakedObject nakedObjectRepresentingCollection, NakedObject element);

    void init(NakedObject nakedObjectRepresentingCollection, NakedObject[] initData);
    
    /**
     * Convenience method that returns the {@link TypeOfFacet} on this
     * facet's {@link #getFacetHolder() holder}.
     */
    TypeOfFacet getTypeOfFacet();
    
}

// Copyright (c) Naked Objects Group Ltd.
