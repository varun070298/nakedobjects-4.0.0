package org.nakedobjects.plugins.dnd;

import junit.framework.TestCase;

import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ContentDragTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testLocation() {
        final DummyView sourceView = new DummyView();
        sourceView.setParent(new DummyWorkspaceView());
        sourceView.setupLocation(new Location(1000, 1000));

        final ContentDrag drag = new ContentDrag(sourceView, new Location(10, 10), new DummyView());
        assertEquals(new Location(10, 10), drag.getOffset());

        final DummyView targetView = new DummyView();
        targetView.setupAbsoluteLocation(new Location(100, 100));

        drag.drag(targetView, new Location(120, 120), 0);

        assertEquals(new Location(20, 20), drag.getTargetLocation());
    }
}

// Copyright (c) Naked Objects Group Ltd.
