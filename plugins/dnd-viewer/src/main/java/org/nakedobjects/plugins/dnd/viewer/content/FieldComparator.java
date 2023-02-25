package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dnd.Comparator;


public class FieldComparator implements Comparator {
    private final NakedObjectAssociation field;
    private String title;

    public FieldComparator(final NakedObjectAssociation field) {
        this.field = field;
    }

    public void init(final NakedObject element) {
        final NakedObject refTo = field.get(element);
        title = refTo == null ? null : refTo.titleString();
        title = title == null ? "" : title;
    }

    public int compare(final NakedObject sortedElement) {
        final NakedObject refTo = field.get(sortedElement);
        String sortedTitle = refTo == null ? null : refTo.titleString();
        sortedTitle = sortedTitle == null ? "" : sortedTitle;
        final int compareTo = sortedTitle.compareTo(title);
        return compareTo;
    }

    public NakedObjectAssociation getField() {
        return field;
    }
}
// Copyright (c) Naked Objects Group Ltd.
