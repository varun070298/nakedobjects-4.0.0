package org.nakedobjects.plugins.dnd.viewer.drawing;

public interface Text {

    /**
     * Returns the width, in pixels, of the specified character.
     */
    int charWidth(char c);

    /**
     * Returns the height, in pixels, of the distance from the baseline to top of the tallest character
     * (including accents that are not common in english.
     */
    int getAscent();

    /**
     * Returns the height, in pixels, of the distance from bottom of the lowest descending character to the
     * baseline.
     */
    int getDescent();

    /**
     * Returns the mid point, in pixels, between the baseline and the top of the characters.
     */
    int getMidPoint();

    /**
     * Return the name of this text style.
     */
    String getName();

    /**
     * Returns the height, in pixels, for a normal line of text - where there is some space between two lines
     * of text.
     */
    int getTextHeight();

    /**
     * Returns the sum of the text height and line spacing.
     * 
     * @see #getLineHeight()
     * @see #getLineSpacing()
     */
    int getLineHeight();

    /**
     * Returns the number of blank vertical pixels to add between adjacent lines to give them additional
     * spacing.
     */
    int getLineSpacing();

    /**
     * Returns the width of the specified in pixels.
     */
    int stringWidth(String text);

    /**
     * Returns the height in pixels when the specified text is wrapped at the specified width
     */
    int stringHeight(String text, int maxWidth);

    int stringWidth(String message, int maxWidth);
}
// Copyright (c) Naked Objects Group Ltd.
