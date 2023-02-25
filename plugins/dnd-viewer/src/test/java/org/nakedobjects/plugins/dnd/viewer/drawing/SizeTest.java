package org.nakedobjects.plugins.dnd.viewer.drawing;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class SizeTest extends TestCase {

    private Size s;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SizeTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        s = new Size(10, 20);
    }

    public void testCopy() {
        final Size m = new Size(s);
        assertTrue(s != m);
        assertEquals(s, m);
    }

    public void testEnsure() {
        s.ensureWidth(18);
        assertEquals(new Size(18, 20), s);
        s.ensureWidth(12);
        assertEquals(new Size(18, 20), s);

        s.ensureHeight(16);
        assertEquals(new Size(18, 20), s);
        s.ensureHeight(26);
        assertEquals(new Size(18, 26), s);
    }

    public void addPadding() {
        s.extend(new Padding(1, 2, 3, 4));
        assertEquals(new Size(14, 26), s);
    }

    public void testExtend() {
        s.extendWidth(8);
        assertEquals(new Size(18, 20), s);

        s.extendHeight(6);
        assertEquals(new Size(18, 26), s);

        s.extend(new Size(3, 5));
        assertEquals(new Size(21, 31), s);

        s.extend(5, 3);
        assertEquals(new Size(26, 34), s);

    }

}
// Copyright (c) Naked Objects Group Ltd.
