package org.nakedobjects.plugins.dnd.viewer.view;

import junit.framework.TestCase;

import org.nakedobjects.plugins.dnd.NullContent;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class AbstractViewTest extends TestCase {
    private AbstractView av;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AbstractViewTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        av = new AbstractView(new NullContent(), null, null) {};
        super.setUp();
    }

    public void testBounds() {
        assertEquals(new Location(), av.getLocation());
        assertEquals(new Size(), av.getSize());
        assertEquals(new Bounds(), av.getBounds());

        av.setLocation(new Location(10, 20));
        assertEquals(new Location(10, 20), av.getLocation());
        assertEquals(new Size(), av.getSize());
        assertEquals(new Bounds(10, 20, 0, 0), av.getBounds());

        av.setSize(new Size(30, 40));
        assertEquals(new Location(10, 20), av.getLocation());
        assertEquals(new Size(30, 40), av.getSize());
        assertEquals(new Bounds(10, 20, 30, 40), av.getBounds());

        av.setBounds(new Bounds(new Location(50, 60), new Size(70, 80)));
        assertEquals(new Location(50, 60), av.getLocation());
        assertEquals(new Size(70, 80), av.getSize());
        assertEquals(new Bounds(50, 60, 70, 80), av.getBounds());
    }

    public void testPadding() {
        assertEquals(new Padding(0, 0, 0, 0), av.getPadding());
    }

    public void testViewAreaType() {
        final Location loc = new Location(10, 10);
        assertEquals(ViewAreaType.CONTENT, av.viewAreaType(loc));
    }
}
// Copyright (c) Naked Objects Group Ltd.
