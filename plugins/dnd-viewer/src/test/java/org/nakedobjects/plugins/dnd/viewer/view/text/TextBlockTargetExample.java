package org.nakedobjects.plugins.dnd.viewer.view.text;

import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlockTarget;


final class TextBlockTargetExample implements TextBlockTarget {
    public int getMaxFieldWidth() {
        return 200;
    }

    public Text getText() {
        return new Text() {

            public int charWidth(final char ch) {
                return 10;
            }

            public int stringHeight(final String text, final int width) {
                return 0;
            }

            public int stringWidth(final String string) {
                return 40;
            }

            public int stringWidth(final String message, final int maxWidth) {
                return 0;
            }

            public int getAscent() {
                return 10;
            }

            public int getLineHeight() {
                return 15;
            }

            public int getMidPoint() {
                return 7;
            }

            public int getDescent() {
                return 0;
            }

            public int getTextHeight() {
                return 15;
            }

            public int getLineSpacing() {
                return 0;
            }

            public String getName() {
                return null;
            }
        };
    }
}

// Copyright (c) Naked Objects Group Ltd.
