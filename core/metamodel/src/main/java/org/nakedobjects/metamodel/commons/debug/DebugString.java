package org.nakedobjects.metamodel.commons.debug;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class DebugString {
    private static final int COLUMN_SPACING = 25;
    private static final int INDENT_WIDTH = 3;
    private static final String LINE;
    private static final int MAX_LINE_LENGTH;
    private static final int MAX_SPACES_LENGTH;
    private static final String SPACES = "                                                                            ";

    static {
        LINE = "-------------------------------------------------------------------------------";
        MAX_LINE_LENGTH = LINE.length();
        MAX_SPACES_LENGTH = SPACES.length();
    }

    private int indent = 0;
    private int section = 1;
    private final StringBuffer string = new StringBuffer();
    private boolean newLine = true;

    /**
     * Append the specified number within a space (number of spaces) specified by the width. E.g. "15 " where
     * number is 15 and width is 4.
     */
    public void append(final int number, final int width) {
        appendIndent();
        final int len = string.length();
        string.append(number);
        regularizeWidth(width, len);
    }

    /**
     * Append the specified object by calling it <code>toString()</code> method.
     */
    public void append(final Object object) {
        if (object instanceof DebugInfo) {
            indent();
            appendTitle(((DebugInfo) object).debugTitle());
            ((DebugInfo) object).debugData(this);
            unindent();
        } else {
            appendIndent();
            string.append(object);
        }
    }

    /**
     * Append the specified object by calling its <code>toString()</code> method, placing it within specified
     * space.
     */
    public void append(final Object object, final int width) {
        appendIndent();
        final int len = string.length();
        string.append(object);
        regularizeWidth(width, len);
    }

    /**
     * Append the specified number, displayed in hexadecimal notation, with the specified label, then start a
     * new line.
     */
    public void appendAsHexln(final String label, final long value) {
        appendln(label, "#" + Long.toHexString(value));
    }

    /**
     * Append the message and trace of the specified exception.
     */
    public void appendException(final Exception e) {
        ByteArrayOutputStream baos;
        final PrintStream s = new PrintStream(baos = new ByteArrayOutputStream());
        e.printStackTrace(s);
        appendln(e.getMessage());
        appendln(new String(baos.toByteArray()));
        s.close();
    }

    /**
     * Start a new line.
     * 
     * @see #blankLine()
     */
    public void appendln() {
        string.append('\n');
        newLine = true;
    }

    /**
     * Append the specified text, then start a new line.
     */
    public void appendln(final String text) {
        appendIndent();
        append(text);
        appendln();
        newLine = true;
    }

    /**
     * Append the specified value, displayed as true or false, with the specified label, then start a new
     * line.
     */
    public void appendln(final String label, final boolean value) {
        appendln(label, String.valueOf(value));
    }

    /**
     * Append the specified number with the specified label, then start a new line.
     */
    public void appendln(final String label, final double value) {
        appendln(label, String.valueOf(value));
    }

    /**
     * Append the specified number, displayed in hexadecimal notation, with the specified label, then start a
     * new line.
     */
    public void appendln(final String label, final long value) {
        appendln(label, String.valueOf(value));
    }

    /**
     * Append the specified object with the specified label, then start a new line.
     */
    public void appendln(final String label, final Object object) {
        appendIndent();
        string.append(label);
        final int spaces = COLUMN_SPACING - label.length();
        string.append(": " + spaces(spaces > 0 ? spaces : 0));
        string.append(object);
        string.append('\n');
        newLine = true;
    }

    /**
     * Append the elements of the specified array with the specified label. Each element is appended on its
     * own line, and a new line is added after the last element.
     */
    public void appendln(final String label, final Object[] object) {
        if (object.length == 0) {
            appendln(label, "empty array");
        } else {
            appendln(label, object[0]);
            for (int i = 1; i < object.length; i++) {
                string.append(spaces(COLUMN_SPACING + 2));
                string.append(object[i]);
                string.append('\n');
            }
            newLine = true;
        }
    }

    /**
     * Append the specified title, then start a new line. A title is shown on two lines with the text on the
     * first line and dashes on the second.
     */
    public void appendTitle(final String title) {
        appendln();
        final String titleString = section++ + ". " + title;
        // string.append('\n');
        appendln(titleString);
        // string.append('\n');
        final String underline = LINE.substring(0, Math.min(MAX_LINE_LENGTH, titleString.length()));
        appendln(underline);
        // string.append('\n');
    }

    public void startSection(final String title) {
        appendTitle(title);
        indent();
    }

    public void endSection() {
        appendln();
        unindent();
    }

    /**
     * Append a blank line only if there are existing lines and the previous line is not blank.
     */
    public void blankLine() {
        final int length = string.length();
        if (length == 0) {
            return;
        }
        final boolean hasLineEnding = string.charAt(length - 1) == '\n';
        if (!hasLineEnding) {
            string.append('\n');
            string.append('\n');
            newLine = true;
        } else {
            final boolean hasDoubleLineEnding = length >= 2 && string.charAt(length - 2) != '\n';
            if (hasDoubleLineEnding) {
                string.append('\n');
                newLine = true;
            }
        }
    }

    /**
     * Increase indent used when appending.
     */
    public void indent() {
        indent++;
    }

    private void appendIndent() {
        if (newLine) {
            final String spaces = spaces(Math.min(MAX_SPACES_LENGTH, indent * INDENT_WIDTH));
            string.append(spaces);
            newLine = false;
        }
    }

    private void regularizeWidth(final int width, final int len) {
        if (width > 0) {
            final int textWidth = string.length() - len;
            if (textWidth > width) {
                string.setLength(len + width - 3);
                string.append("...");
            } else {
                int spaces = width - textWidth;
                spaces = Math.max(0, spaces);
                string.append(SPACES.substring(0, spaces));
            }
        }
    }

    private String spaces(final int spaces) {
        return SPACES.substring(0, spaces);
    }

    /**
     * Return the <code>String</code> representation of this debug string.
     */
    @Override
    public String toString() {
        return string.toString();
    }

    /**
     * Decrease indent used when appending.
     */
    public void unindent() {
        if (indent > 0) {
            indent--;
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
