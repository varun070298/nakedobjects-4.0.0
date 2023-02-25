package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class DragViewOutline extends AbstractView {
    private final int thickness = 5;
    private final Size size;

    public DragViewOutline(final View view) {
        super(view.getContent(), null, null);
        size = view.getSize();
        setLocation(view.getAbsoluteLocation());
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final Bounds r = getBounds();

        for (int i = 0; i < thickness; i++) {
            canvas.drawRectangle(i, i, r.getWidth() - i * 2 - 1, r.getHeight() - i * 2 - 1, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1));
        }
    }

    @Override
    public Size getMaximumSize() {
        return new Size(size);
    }
}
// Copyright (c) Naked Objects Group Ltd.
