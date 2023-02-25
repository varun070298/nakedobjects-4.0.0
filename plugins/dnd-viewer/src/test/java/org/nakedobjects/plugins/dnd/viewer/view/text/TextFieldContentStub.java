package org.nakedobjects.plugins.dnd.viewer.view.text;

import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;

public class TextFieldContentStub extends TextContent {

    public TextFieldContentStub() {
        super(null, 1, WRAPPING);
    }

    @Override
    public void alignDisplay(final int line) {}

    @Override
    public int getNoLinesOfContent() {
        return 16;
    }

    @Override
    public int getNoDisplayLines() {
        return 3;
    }

    @Override
    public String getText(final int forLine) {
        // 35 characters
        // 0 - 3 Now
        // 4 - 6 is
        // 7 - 10 the
        // 11 - 17 winter
        // 18 - 20 of
        // 21 - 25 our
        // 24 - 34 discontent
        return "Now is the winter of our discontent";
    }
}
// Copyright (c) Naked Objects Group Ltd.
