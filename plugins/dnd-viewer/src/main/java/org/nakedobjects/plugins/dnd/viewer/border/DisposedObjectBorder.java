package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class DisposedObjectBorder extends AbstractBorder {

    public DisposedObjectBorder(final int size, final View wrappedView) {
        super(wrappedView);
        top = size;
        left = size;
        bottom = size;
        right = size;
    }

    public DisposedObjectBorder(final View wrappedView) {
        this(2, wrappedView);
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        debug.append("DisposedObjectBorder " + top + " pixels");
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        return super.dragStart(drag);
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        Color color = null;
        color = Toolkit.getColor(ColorsAndFonts.COLOR_INVALID);
        final Size s = getSize();

        final int w = s.getWidth();
        final int xExtent = s.getWidth() - left;
        for (int i = 0; i < left; i++) {
            canvas.drawRectangle(i, i, xExtent - 2 * i, s.getHeight() - 2 * i, color);
        }
        for (int i = 0; i < 15; i++) {
            canvas.drawLine(left, top + i, left + i, top, color);
            canvas.drawLine(w - left - right - 1, s.getHeight() - top - i - 1, w - left - right - i - 1, s.getHeight() - top - 1,
                    color);
        }
    }

    @Override
    public void entered() {
        wrappedView.entered();
        getFeedbackManager().setError("Destroyed objects cannot be used");
        markDamaged();
    }

    @Override
    public void exited() {
        wrappedView.exited();
        getFeedbackManager().setError("");
        markDamaged();
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/DisposedObjectBorder [" + getSpecification() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
