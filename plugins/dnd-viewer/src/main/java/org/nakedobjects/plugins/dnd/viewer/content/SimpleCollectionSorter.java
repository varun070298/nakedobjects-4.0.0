package org.nakedobjects.plugins.dnd.viewer.content;

import java.util.Enumeration;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.CollectionSorter;
import org.nakedobjects.plugins.dnd.Comparator;


public class SimpleCollectionSorter implements CollectionSorter {

    public void sort(final NakedObject[] elements, final Comparator order, final boolean reverse) {
        if (order == null) {
            return;
        }

        final Vector sorted = new Vector(elements.length);
        outer: for (int j = 0; j < elements.length; j++) {
            final NakedObject element = elements[j];
            order.init(element);
            int i = 0;
            for (final Enumeration f = sorted.elements(); f.hasMoreElements();) {
                final NakedObject sortedElement = (NakedObject) f.nextElement();
                if (sortedElement != null && (order.compare(sortedElement) > 0 ^ reverse)) {
                    sorted.insertElementAt(element, i);
                    continue outer;
                }
                i++;
            }
            sorted.addElement(element);
        }
        sorted.copyInto(elements);
    }

}
// Copyright (c) Naked Objects Group Ltd.
