package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;


public interface Comparator {
    void init(NakedObject element);

    int compare(NakedObject sortedElement);
}
// Copyright (c) Naked Objects Group Ltd.
