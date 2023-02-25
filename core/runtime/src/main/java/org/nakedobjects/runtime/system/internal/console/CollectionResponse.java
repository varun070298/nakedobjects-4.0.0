package org.nakedobjects.runtime.system.internal.console;

import java.io.Serializable;
import java.util.Enumeration;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;


public class CollectionResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    Object oid;
    Object elements[];

    public CollectionResponse(final NakedObject collection) {
        oid = collection.getOid();
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final Enumeration elements = facet.elements(collection);
        int count = 0;
        while (elements.hasMoreElements()) {
            final NakedObject obj = (NakedObject) elements.nextElement();
            this.elements[count] = obj.getOid();
            count++;
        }
    }

    public java.lang.Object[] getElements() {
        return elements;
    }

    public java.lang.Object getOid() {
        return oid;
    }
}

// Copyright (c) Naked Objects Group Ltd.
