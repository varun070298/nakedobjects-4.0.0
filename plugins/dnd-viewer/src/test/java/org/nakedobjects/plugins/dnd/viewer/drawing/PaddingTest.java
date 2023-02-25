package org.nakedobjects.plugins.dnd.viewer.drawing;

import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;

import junit.framework.TestCase;


public class PaddingTest extends TestCase {

    private Padding p;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PaddingTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        p = new Padding(2, 3, 4, 5);
    }

    public void testCopy() {
        final Padding q = new Padding(p);
        assertTrue(p != q);
        assertEquals(p, q);
    }

    public void testValues() {
        assertEquals(2, p.getTop());
        assertEquals(3, p.getLeft());
        assertEquals(4, p.getBottom());
        assertEquals(5, p.getRight());
    }

    public void testExtend() {
        p.extendTop(10);
        assertEquals(new Padding(12, 3, 4, 5), p);

        p.extendLeft(10);
        assertEquals(new Padding(12, 13, 4, 5), p);

        p.extendBottom(10);
        assertEquals(new Padding(12, 13, 14, 5), p);

        p.extendRight(10);
        assertEquals(new Padding(12, 13, 14, 15), p);
    }
}
// Copyright (c) Naked Objects Group Ltd.
