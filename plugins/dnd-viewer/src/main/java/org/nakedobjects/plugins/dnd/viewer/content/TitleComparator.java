package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.Comparator;


public class TitleComparator implements Comparator {
    private String title;

    public void init(final NakedObject element) {
        title = element.titleString();
    }

    public int compare(final NakedObject sortedElement) {
        final String sortedTitle = sortedElement.titleString();
        return sortedTitle.compareTo(title);
    }
}
// Copyright (c) Naked Objects Group Ltd.
