package org.nakedobjects.plugins.dnd.viewer.view.text;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlock;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlockTarget;


public class TextBlockTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TextBlockTest.class);
    }

    private TextBlock block;

    @Override
    protected void setUp() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);

        final TextBlockTarget user = new TextBlockTargetExample();
        block = new TextBlock(user, "Now is the winter of our discontent made summer by this glorious sun of York", true);
        // "Now is the winter "
        // "of our discontent "
        // "made summer by "
        // "this glorious sun "
        // "of York"
    }

    public void testBreakBlock() {
        final TextBlock newBlock = block.splitAt(1, 11);
        assertEquals("Now is the winter ", block.getLine(0));
        assertEquals("of our disc", block.getLine(1));

        assertEquals("ontent made summer ", newBlock.getLine(0));

    }

    public void testCantInsertNewline() {
        try {
            block.insert(0, 4, "\n");
            fail();
        } catch (final IllegalArgumentException expected) {}
    }

    public void testCountLine() {
        assertEquals(5, block.noLines());
    }

    public void testDeletePartOfLine() {
        block.delete(0, 4, 0, 11);
        assertEquals("Now winter of our ", block.getLine(0));
        assertEquals("discontent made ", block.getLine(1));

        block.delete(1, 3, 1, 10);
        assertEquals("dis made summer by ", block.getLine(1));
    }

    public void testDeleteLines() {
        block.delete(0, 4, 2, 12);
        // assertEquals("Now ", block.getLine(0));
        assertEquals("Now by this ", block.getLine(0));
        assertEquals("glorious sun of York", block.getLine(1));
    }

    public void testDeleteLeft() {
        block.deleteLeft(0, 3);
        assertEquals("No is the winter of ", block.getLine(0));

        block.deleteLeft(0, 17);
        assertEquals("No is the winterof ", block.getLine(0));
        assertEquals("our discontent ", block.getLine(1));

        block.deleteLeft(1, 9);
        assertEquals("our discntent ", block.getLine(1));
    }

    public void testDeleteRight() {
        block.deleteRight(0, 3);
        assertEquals("Nowis the winter of ", block.getLine(0));

        block.deleteRight(0, 17);
        assertEquals("Nowis the winter f ", block.getLine(0));
        assertEquals("our discontent ", block.getLine(1));

        block.deleteRight(1, 9);
        assertEquals("our discotent ", block.getLine(1));
    }

    public void testGetLine() {
        assertEquals("Now is the winter ", block.getLine(0));
        assertEquals("of our discontent ", block.getLine(1));
        assertEquals("made summer by ", block.getLine(2));
        assertEquals("this glorious sun ", block.getLine(3));
        assertEquals("of York", block.getLine(4));
    }

    public void testGetText() {
        assertEquals("Now is the winter of our discontent made summer by this glorious sun of York", block.getText());
    }

    public void testInsert() {
        block.insert(0, 0, "Quote:");
        assertEquals("Quote:Now is the ", block.getLine(0));

        block.insert(1, 10, "y");
        assertEquals("Quote:Now is the ", block.getLine(0));
        assertEquals("winter of your ", block.getLine(1));
    }

}
// Copyright (c) Naked Objects Group Ltd.
