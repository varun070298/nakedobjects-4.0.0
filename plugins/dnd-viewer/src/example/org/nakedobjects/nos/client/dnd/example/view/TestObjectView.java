package org.nakedobjects.viewer.dnd.example.view;

import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.viewer.dnd.Canvas;
import org.nakedobjects.viewer.dnd.Click;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.Drag;
import org.nakedobjects.viewer.dnd.DragStart;
import org.nakedobjects.viewer.dnd.Toolkit;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.view.simple.AbstractView;


public class TestObjectView extends AbstractView {

    private int requiredWidth;
    private int requiredHeight;
    private final String label;

    public TestObjectView(final Content content, final ViewSpecification specification, final ViewAxis axis, final int width, final int height, final String label) {
        super(content, specification, axis);
        this.requiredWidth = width;
        this.requiredHeight = height;
        this.label = label;
    }

    public void draw(final Canvas canvas) {
        super.draw(canvas);
        int width = getSize().getWidth();
        int height = getSize().getHeight();
        canvas.clearBackground(this, Toolkit.getColor(0xeeeeee));
        canvas.drawRectangle(0, 0, width - 1, height - 1, Toolkit.getColor(0xcccccc));
        canvas.drawLine(0, 0, width - 1, height - 1, Toolkit.getColor(0xff0000));
        canvas.drawLine(width - 1, 0, 0, height - 1, Toolkit.getColor(0xff0000));
        canvas.drawText(label, 2, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getAscent() + 2, Toolkit.getColor(0), Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
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
        super.firstClick(click);
    }

    public void secondClick(final Click click) {
        debug("second click " + click);
        super.secondClick(click);
    }

    public void mouseMoved(final Location location) {
        debug("mouse moved " + location);
        super.mouseMoved(location);
    }

    private void debug(final String str) {
        getViewManager().getSpy().addAction(str);
    }

    public Drag dragStart(final DragStart drag) {
        debug("drag start " + drag);
        return super.dragStart(drag);
    }
}
// Copyright (c) Naked Objects Group Ltd.
