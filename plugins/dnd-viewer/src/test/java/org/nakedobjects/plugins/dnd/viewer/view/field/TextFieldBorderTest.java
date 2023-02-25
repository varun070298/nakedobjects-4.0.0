package org.nakedobjects.plugins.dnd.viewer.view.field;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.plugins.dnd.DummyView;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.view.field.TextFieldBorder;


public class TextFieldBorderTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TextFieldBorderTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
    }

    public void testBorder() {
        final DummyView mockView = new DummyView();
        final TextFieldBorder border = new TextFieldBorder(mockView);
        assertEquals(new Padding(2, 2, 2, 2), border.getPadding());
    }
}
// Copyright (c) Naked Objects Group Ltd.
