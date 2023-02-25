package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.DragViewOutline;

/**
 * A drag view border provides a line and handle that appears when the mouse moves over the contained view
 * and allows the view to be dragged.
 */
public class DragViewBorder extends AbstractBorder {
    private final int handleWidth = 14;

    public DragViewBorder(final View wrappedView) {
        this(1, wrappedView);
    }

    public DragViewBorder(final int size, final View wrappedView) {
        super(wrappedView);

        top = size;
        left = size;
        bottom = size;
        right = size + handleWidth;
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        debug.append("SimpleBorder " + top + " pixels\n");
        debug.append("           handle " + handleWidth + " pixels");
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (overBorder(drag.getLocation())) {
            final Location location = drag.getLocation();
            final DragViewOutline dragOverlay = new DragViewOutline(getView());
            return new ViewDrag(this, new Offset(location.getX(), location.getY()), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    @Override
    public void entered() {
        getState().setContentIdentified();
        getState().setViewIdentified();
        wrappedView.entered();
        markDamaged();
    }

    @Override
    public void exited() {
        getState().clearObjectIdentified();
        getState().clearViewIdentified();
        wrappedView.exited();
        markDamaged();
    }

    @Override
    public void draw(final Canvas canvas) {
        if (getState().isViewIdentified()) {
            final Color color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
            final Size s = getSize();
            final int width = s.getWidth();
            for (int i = 0; i < left; i++) {
                canvas.drawRectangle(i, i, width - 2 * i - 1, s.getHeight() - 2 * i - 1, color);
            }
            final int w2 = width - left - 2;
            final int w3 = w2 - handleWidth;
            for (int x = w2; x > w3; x -= 2) {
                canvas.drawLine(x, top, x, s.getHeight() - top, color);
            }
        }
        super.draw(canvas);
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/SimpleBorder";
    }
}
// Copyright (c) Naked Objects Group Ltd.
