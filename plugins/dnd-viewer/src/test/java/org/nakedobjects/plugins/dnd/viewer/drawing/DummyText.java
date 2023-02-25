package org.nakedobjects.plugins.dnd.viewer.drawing;

import org.nakedobjects.plugins.dnd.viewer.drawing.Text;

public class DummyText implements Text {

    public DummyText() {
        super();
    }

    public int charWidth(final char c) {
        return 10;
    }

    public int getAscent() {
        return 2;
    }

    public int getDescent() {
        return 4;
    }

    public int getMidPoint() {
        return 1;
    }

    public int getTextHeight() {
        return 8;
    }

    public int getLineHeight() {
        return getAscent() + getTextHeight() + getDescent();
    }

    public int getLineSpacing() {
        return getLineHeight() + 5;
    }

    public int stringHeight(final String text, final int width) {
        return getLineHeight();
    }

    public int stringWidth(final String text) {
        return text.length() * charWidth('x');
    }

    public int stringWidth(final String message, final int maxWidth) {
        return 0;
    }

    public String getName() {
        return null;
    }

}
// Copyright (c) Naked Objects Group Ltd.
