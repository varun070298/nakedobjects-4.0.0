package org.nakedobjects.plugins.dnd.viewer.basic;

import junit.framework.TestCase;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.CollectionSorter;
import org.nakedobjects.plugins.dnd.viewer.content.SimpleCollectionSorter;
import org.nakedobjects.plugins.dnd.viewer.content.TitleComparator;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;


public class SimpleCollectionSorterTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SimpleCollectionSorterTest.class);
    }

    public void testSortByTitle() {
        final NakedObject[] instances = new NakedObject[] { object("one"), object("two"), object("three"), object("four"), };

        final SimpleCollectionSorter sorter = new SimpleCollectionSorter();
        sorter.sort(instances, new TitleComparator(), CollectionSorter.NORMAL);

        assertEquals("four", instances[0].titleString());
        assertEquals("one", instances[1].titleString());
        assertEquals("three", instances[2].titleString());
        assertEquals("two", instances[3].titleString());
    }

    public void testSortByTitleReversed() {
        final NakedObject[] instances = new NakedObject[] { object("one"), object("two"), object("three"), object("four"), };

        final SimpleCollectionSorter sorter = new SimpleCollectionSorter();
        sorter.sort(instances, new TitleComparator(), CollectionSorter.REVERSED);

        assertEquals("two", instances[0].titleString());
        assertEquals("three", instances[1].titleString());
        assertEquals("one", instances[2].titleString());
        assertEquals("four", instances[3].titleString());
    }

    private NakedObject object(final String string) {
        final TestProxyNakedObject object = new TestProxyNakedObject();
        object.setupTitleString(string);
        return object;
    }
}
// Copyright (c) Naked Objects Group Ltd.
