package org.nakedobjects.plugins.dnd.viewer.view.text;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class TextSelection {
    private final CursorPosition cursor;
    private final CursorPosition start;

    public TextSelection(final TextContent content) {
        this.cursor = new CursorPosition(content, 0, 0);
        this.start = new CursorPosition(content, 0, 0);
    }

    /**
     * Determine if the selection is back to front. Returns true if the cursor position is before the start
     * position.
     */
    private boolean backwardSelection() {
        return cursor.isBefore(start);
    }

    public void extendTo(final CursorPosition pos) {
        cursor.asFor(pos);
    }

    /**
     * extends the selection so the end point is the same as the cursor.
     */
    public void extendTo(final Location at) {
        cursor.cursorAt(at);
    }

    public CursorPosition from() {
        return backwardSelection() ? cursor : start;
    }

    // private CursorPosition end = new CursorPosition(0,0);

    /**
     * returns true is a selection exists - if the start and end locations are not the same
     */
    public boolean hasSelection() {
        return !cursor.samePosition(start);
    }

    /**
     * clears the selection so nothing is selected. The start and end points are set to the same values as the
     * cursor.
     */
    public void resetTo(final CursorPosition pos) {
        start.asFor(pos);
        cursor.asFor(pos);
    }

    public void selectSentence() {
        resetTo(cursor);
        start.home();
        cursor.end();
    }

    /**
     * set the selection to be for the word marked by the current cursor
     * 
     */
    public void selectWord() {
        resetTo(cursor);
        start.wordLeft();
        cursor.wordRight();
    }

    public CursorPosition to() {
        return backwardSelection() ? start : cursor;
    }

    @Override
    public String toString() {
        return "Selection [from=" + start.getLine() + ":" + start.getCharacter() + ",to=" + cursor.getLine() + ":"
                + cursor.getCharacter() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
