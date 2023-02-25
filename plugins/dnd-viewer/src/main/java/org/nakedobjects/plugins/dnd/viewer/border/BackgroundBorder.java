package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;

/**
 * A background border provides a coloured background to a view of a specified colour. 
 */
public class BackgroundBorder extends AbstractBorder {
    private Color background;

    /**
     * Creates a background border with a default colour of white.
     */
    public BackgroundBorder(final View wrappedView) {
        super(wrappedView);
        background = Toolkit.getColor(ColorsAndFonts.COLOR_WHITE);
    }

    public BackgroundBorder(final Color background, final View wrappedView) {
        super(wrappedView);
        this.background = background;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.clearBackground(this, background);
        super.draw(canvas);
    }

    public void setBackground(final Color color) {
        this.background = color;
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/BackgroundBorder";
    }
}
// Copyright (c) Naked Objects Group Ltd.
