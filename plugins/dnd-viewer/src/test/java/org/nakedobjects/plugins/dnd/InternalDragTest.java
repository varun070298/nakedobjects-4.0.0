package org.nakedobjects.plugins.dnd;

import junit.framework.TestCase;

import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.SimpleInternalDrag;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class InternalDragTest extends TestCase {
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(InternalDragTest.class);
    }

    public void testDragStart() {
        final DummyView view = new DummyView();
        view.setupAbsoluteLocation(new Location(30, 60));

        final InternalDrag id = new SimpleInternalDrag(view, new Location(100, 110));
        assertEquals(new Location(70, 50), id.getLocation());

        id.drag(null, new Location(110, 130), 0);
        assertEquals(new Location(80, 70), id.getLocation());
    }
}
// Copyright (c) Naked Objects Group Ltd.
