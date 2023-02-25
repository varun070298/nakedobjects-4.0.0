package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.text.CursorPosition;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;


public class WrappedTextField extends TextField {
    private static final Logger LOG = Logger.getLogger(WrappedTextField.class);

    public WrappedTextField(
            final TextParseableContent content,
            final ViewSpecification specification,
            final ViewAxis axis,
            final boolean showLines) {
        super(content, specification, axis, showLines, TextContent.WRAPPING);
    }

    public void setWrapping(final boolean wrapping) {}

    @Override
    protected void drawLines(final Canvas canvas, final Color color, final int width) {
        int baseline = getBaseline();
        final int noDisplayLines = textContent.getNoDisplayLines();
        for (int line = 0; line < noDisplayLines; line++) {
            canvas.drawLine(HPADDING, baseline, HPADDING + width, baseline, color);
            baseline += getText().getLineHeight();
        }
    }

    @Override
    protected void drawHighlight(final Canvas canvas, final int maxWidth) {
        final int baseline = getBaseline();
        int top = baseline - style.getAscent();

        final CursorPosition from = selection.from();
        final CursorPosition to = selection.to();

        final String[] lines = textContent.getDisplayLines();
        final int displayFromLine = textContent.getDisplayFromLine();
        final int displayToLine = displayFromLine + lines.length;
        for (int i = displayFromLine; i <= displayToLine; i++) {
            if ((i >= from.getLine()) && (i <= to.getLine())) {
                final String line = textContent.getText(i);
                int start = 0;
                int end = style.stringWidth(line);

                if (from.getLine() == i) {
                    final int at = Math.min(from.getCharacter(), line.length());
                    start = style.stringWidth(line.substring(0, at));
                }

                if (to.getLine() == i) {
                    final int at = Math.min(to.getCharacter(), line.length());
                    end = style.stringWidth(line.substring(0, at));
                }

                canvas.drawSolidRectangle(start + (HPADDING), top, end - start, getText().getLineHeight(), Toolkit
                        .getColor(ColorsAndFonts.COLOR_TEXT_HIGHLIGHT));
            }

            top += getText().getLineHeight();
        }
    }

    @Override
    protected void drawText(final Canvas canvas, final Color textColor, final int width) {
        int baseline = getBaseline();
        final String[] lines = textContent.getDisplayLines();
        final int cursorLine = cursor.getLine() - textContent.getDisplayFromLine();
        for (int i = 0; i < lines.length; i++) {
            final String chars = lines[i];
            if (chars == null) {
                throw new NakedObjectException();
            }
            if (chars.endsWith("\n")) {
                throw new RuntimeException();
            }

            // draw cursor
            if (hasFocus() && canChangeValue().isAllowed() && cursorLine == i) {
                final int at = Math.min(cursor.getCharacter(), chars.length());
                final int pos = style.stringWidth(chars.substring(0, at)) + HPADDING;
                canvas.drawLine(pos, (baseline + style.getDescent()), pos, baseline - style.getAscent(), Toolkit
                        .getColor(ColorsAndFonts.COLOR_TEXT_CURSOR));
            }

            // draw text
            canvas.drawText(chars, HPADDING, baseline, textColor, style);
            baseline += getText().getLineHeight();
        }
        /*
         * if (end < entryLength) { int x = style.stringWidth(new String(buffer, start, end));
         * g.setColor(Color.red); g.drawString("\u00bb", x, baseline - lineHeight()); }
         */
    }

    @Override
    protected boolean enter() {
        textContent.breakBlock(cursor);
        cursor.lineDown();
        cursor.home();
        markDamaged();
        return true;
    }

    /**
     * Sets the number of lines to display
     */
    public void setNoLines(final int noLines) {
        textContent.setNoDisplayLines(noLines);
    }

    @Override
    public void setSize(final Size size) {
        super.setSize(size);
        textContent.setNoDisplayLines(size.getHeight() / style.getLineHeight());
    }

    @Override
    public void setMaximumSize(final Size size) {
        final int lines = Math.max(1, size.getHeight() / getText().getLineHeight());
        setNoLines(lines);
        final int width = Math.max(180, size.getWidth() - HPADDING);
        setWidth(width);
        LOG.debug(lines + " x " + width);
        invalidateLayout();
    }

    @Override
    protected void align() {}

}
// Copyright (c) Naked Objects Group Ltd.
