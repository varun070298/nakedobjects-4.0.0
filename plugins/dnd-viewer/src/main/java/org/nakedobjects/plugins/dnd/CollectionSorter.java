package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;


public interface CollectionSorter {
    boolean REVERSED = true;
    boolean NORMAL = false;

    /**
     * Sorts the array of objects into the order specified by the comparator.
     */
    void sort(final NakedObject[] element, Comparator order, boolean reverse);
}
// Copyright (c) Naked Objects Group Ltd.
