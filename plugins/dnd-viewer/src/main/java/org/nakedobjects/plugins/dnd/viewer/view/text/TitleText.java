package org.nakedobjects.plugins.dnd.viewer.view.text;

import org.nakedobjects.metamodel.adapter.ResolveException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


/**
 * TitleText draws the text derived from the subclass within a view. The text is properly truncated if longer
 * than the specified maximum width.
 */
public abstract class TitleText {
    private static final int NO_MAX_WIDTH = -1;
    private final int ellipsisWidth;
    private final Color color;
    private final Text style;
    private final View view;
    private boolean resolveFailure;

    public TitleText(final View view, final Text style, final Color color) {
        this.view = view;
        this.style = style;
        this.color = color;
        ellipsisWidth = style.stringWidth("...");
    }

    /**
     * Draw this TitleText's text stating from the specified x coordination and on the specified baseline.
     */
    public void draw(final Canvas canvas, final int x, final int baseline) {
        draw(canvas, x, baseline, NO_MAX_WIDTH);
    }

    /**
     * Draw this TitleText's text stating from the specified x coordination and on the specified baseline. If
     * a maximum width is specified (ie it is positive) then the text drawn will not extend past that width.
     * 
     * @param maxWidth
     *            the maximum width to display the text within; if negative no limit is imposed
     */
    public void draw(final Canvas canvas, final int x, final int baseline, final int maxWidth) {
        Color color;
        final ViewState state = view.getState();
        if (resolveFailure) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_ERROR);
        } else if (state.canDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_VALID);
        } else if (state.cantDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_INVALID);
        } else if (state.isObjectIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);
        } else {
            color = this.color;
        }

        final int xt = x;
        final int yt = baseline;

        String text = getTitle();
        if (maxWidth > 0 && style.stringWidth(text) > maxWidth) {
            int lastCharacterWithinAllowedWidth = 0;
            for (int textWidth = ellipsisWidth; textWidth <= maxWidth;) {
                final char character = text.charAt(lastCharacterWithinAllowedWidth);
                textWidth += style.charWidth(character);
                lastCharacterWithinAllowedWidth++;
            }

            int space = text.lastIndexOf(' ', lastCharacterWithinAllowedWidth - 1);
            if (space > 0) {
                while (space >= 0) {
                    final char character = text.charAt(space - 1);
                    if (Character.isLetterOrDigit(character)) {
                        break;
                    }
                    space--;
                }

                text = text.substring(0, space);
            } else {
                text = text.substring(0, lastCharacterWithinAllowedWidth - 1);
            }
            text += "...";
        }

        if (Toolkit.debug) {
            final int x2 = style.stringWidth(text);
            canvas.drawDebugOutline(new Bounds(xt, yt - style.getAscent(), x2, style.getTextHeight()), baseline, Toolkit
                    .getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_DRAW));
        }
        canvas.drawText(text, xt, yt, color, style);
    }

    public Size getSize() {
        final int height = style.getTextHeight();
        final int width = style.stringWidth(getTitle());
        return new Size(width, height);
    }

    private String getTitle() {
        if (resolveFailure) {
            return "Resolve Failure!";
        }

        String title;
        try {
            title = title();
        } catch (final ResolveException e) {
            resolveFailure = true;
            title = "Resolve Failure!";
        }
        return title;
    }

    protected abstract String title();

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("style", style);
        str.append("color", color);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
