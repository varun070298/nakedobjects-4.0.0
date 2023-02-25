package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;


public class SingleLineTextField extends TextField {
    private static final int LIMIT = 20;
    private int offset = 0;

    public SingleLineTextField(
            final TextParseableContent content,
            final ViewSpecification specification,
            final ViewAxis axis,
            final boolean showLines) {
        super(content, specification, axis, showLines, TextContent.NO_WRAPPING);
    }

    @Override
    protected void align() {
        final String line = textContent.getText(0);
        if (line != null) {
            final int maxWidth = getMaxFieldWidth();
            final int leftLimit = offset + LIMIT;
            final int rightLimit = offset + maxWidth - LIMIT;

            if (cursor.getCharacter() > line.length()) {
                cursor.end();
            }

            final int cursorPosition = style.stringWidth(line.substring(0, cursor.getCharacter()));
            if (cursorPosition > rightLimit) {
                offset = offset + (cursorPosition - rightLimit);
                offset = Math.min(style.stringWidth(line), offset);
            } else if (cursorPosition < leftLimit) {
                offset = offset - (leftLimit - cursorPosition);
                offset = Math.max(0, offset);
            }
        }
    }

    @Override
    protected void drawHighlight(final Canvas canvas, final int maxWidth) {
        final int baseline = getBaseline();
        final int top = baseline - style.getAscent();

        int from = selection.from().getCharacter();
        int to = selection.to().getCharacter();

        final String line = textContent.getText(0);
        if (to >= line.length()) {
            to = line.length();
        }
        if (from >= line.length()) {
            from = line.length();
        }
        if (line != null) {
            final int start = style.stringWidth(line.substring(0, from));
            final int end = style.stringWidth(line.substring(0, to));
            canvas.drawSolidRectangle(start + (HPADDING), top, end - start, style.getLineHeight(), Toolkit
                    .getColor(ColorsAndFonts.COLOR_TEXT_HIGHLIGHT));
        }
    }

    @Override
    protected void drawLines(final Canvas canvas, final Color color, final int width) {
        final int baseline = getBaseline();
        canvas.drawLine(HPADDING, baseline, HPADDING + width, baseline, color);
    }

    @Override
    protected void drawText(final Canvas canvas, final Color textColor, final int width) {
        final String[] lines = textContent.getDisplayLines();
        if (lines.length > 1) {
            throw new NakedObjectException(
                    "Single line text field should contain a string that contains no line breaks; contains " + lines.length);
        }

        final String chars = lines[0];
        if (chars == null) {
            throw new NakedObjectException();
        }
        if (chars.endsWith("\n")) {
            throw new RuntimeException();
        }

        final int baseline = getBaseline();

        // draw cursor
        if (hasFocus() && canChangeValue().isAllowed()) {
            final int at = Math.min(cursor.getCharacter(), chars.length());
            final int pos = style.stringWidth(chars.substring(0, at)) - offset + HPADDING;
            canvas.drawLine(pos, (baseline + style.getDescent()), pos, baseline - style.getAscent(), Toolkit
                    .getColor(ColorsAndFonts.COLOR_TEXT_CURSOR));
        }

        // draw text
        canvas.drawText(chars, HPADDING - offset, baseline, textColor, style);
    }

    @Override
    public void setMaximumSize(final Size size) {
        final int width = Math.max(180, size.getWidth() - HPADDING);
        setWidth(width);
        invalidateLayout();
    }

}
// Copyright (c) Naked Objects Group Ltd.
