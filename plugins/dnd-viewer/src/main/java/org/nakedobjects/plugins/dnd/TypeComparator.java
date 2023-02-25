package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;


public class TypeComparator implements Comparator {
    private String type;

    public void init(final NakedObject element) {
        type = element.getSpecification().getShortName();
    }

    public int compare(final NakedObject sortedElement) {
        final String sortedType = sortedElement.getSpecification().getShortName();
        return sortedType.compareTo(type);
    }

}
// Copyright (c) Naked Objects Group Ltd.
