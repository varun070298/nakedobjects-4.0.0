package org.nakedobjects.plugins.dnd.viewer.drawing;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;

import junit.framework.TestCase;


public class LocationTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(LocationTest.class);
    }

    public void testCopy() {
        final Location l = new Location(10, 20);
        final Location m = new Location(l);
        assertTrue(l != m);
        assertEquals(l, m);
    }

    public void testTranslate() {
        final Location l = new Location(10, 20);
        l.move(5, 10);
        assertEquals(new Location(15, 30), l);
        l.move(-10, -5);
        assertEquals(new Location(5, 25), l);
    }
}
// Copyright (c) Naked Objects Group Ltd.
