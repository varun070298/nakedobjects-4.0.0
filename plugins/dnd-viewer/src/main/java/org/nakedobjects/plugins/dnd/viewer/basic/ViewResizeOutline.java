package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.NullContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class ViewResizeOutline extends AbstractView {
    private final int thickness = 1;
    private String label = "";
    private final Size size;

    protected ViewResizeOutline(final Bounds resizeArea) {
        super(new NullContent());
        size = resizeArea.getSize();
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final Size s = getSize();
        // Logger.getLogger(getClass()).debug("drag outline size " + s);
        Color color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2);
        for (int i = 0; i < thickness; i++) {
            canvas.drawRectangle(i, i, s.getWidth() - i * 2 - 1, s.getHeight() - i * 2 - 1, color);
        }
        canvas.drawText(label, 2, 16, color, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
    }

    public void setDisplay(final String label) {
        this.label = label == null ? "" : label;
    }

    @Override
    public void dispose() {
        getFeedbackManager().showDefaultCursor();
        super.dispose();
    }

    @Override
    public Size getMaximumSize() {
        return new Size(size);
    }
}
// Copyright (c) Naked Objects Group Ltd.
