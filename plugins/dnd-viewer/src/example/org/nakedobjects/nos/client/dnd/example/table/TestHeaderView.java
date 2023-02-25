package org.nakedobjects.viewer.dnd.example.table;

import org.nakedobjects.viewer.dnd.Canvas;
import org.nakedobjects.viewer.dnd.Click;
import org.nakedobjects.viewer.dnd.Drag;
import org.nakedobjects.viewer.dnd.DragStart;
import org.nakedobjects.viewer.dnd.InternalDrag;
import org.nakedobjects.viewer.dnd.SimpleInternalDrag;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.view.simple.AbstractView;
import org.nakedobjects.viewer.dnd.viewer.AwtColor;


public class TestHeaderView extends AbstractView {

    private int requiredWidth;
    private int requiredHeight;

    public TestHeaderView(final ViewAxis axis, final int width, final int height) {
        super(null, null, axis);
        setMaximumSize(new Size(width, height));
    }

    public void draw(final Canvas canvas) {
        super.draw(canvas);
        int width = getSize().getWidth();
        int height = getSize().getHeight();
        canvas.clearBackground(this, AwtColor.GRAY);
        canvas.drawRectangle(0, 0, width - 1, height - 1, AwtColor.BLUE);
        canvas.drawLine(0, 0, width - 1, height - 1, AwtColor.ORANGE);
        canvas.drawLine(width - 1, 0, 0, height - 1, AwtColor.ORANGE);
    }

    public Size getRequiredSize(final Size maximumSize) {
        return new Size(requiredWidth, requiredHeight);
    }

    public void setMaximumSize(final Size size) {
        requiredHeight = size.getHeight();
        requiredWidth = size.getWidth();

        setSize(size);
    }

    public void firstClick(final Click click) {
        debug("first click " + click);
    }

    public void secondClick(final Click click) {
        debug("second click " + click);
    }

    public void mouseMoved(final Location location) {
        debug("mouse moved " + location);
    }

    private void debug(final String str) {
        getViewManager().getSpy().addAction(str);
    }

    public Drag dragStart(final DragStart drag) {
        debug("drag start in header " + drag);
        return new SimpleInternalDrag(getParent(), drag.getLocation());
    }

    public void drag(final InternalDrag drag) {
        debug("drag in header " + drag);
        super.drag(drag);
    }

    public void dragTo(final InternalDrag drag) {
        debug("drag to in header " + drag);
    }
}
// Copyright (c) Naked Objects Group Ltd.
