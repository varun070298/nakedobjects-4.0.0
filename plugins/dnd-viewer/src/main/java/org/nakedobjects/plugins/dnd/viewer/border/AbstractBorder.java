package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class AbstractBorder extends AbstractViewDecorator {
    protected int bottom;
    protected int left;
    private boolean onBorder;
    protected int right;
    protected int top;

    protected AbstractBorder(final View view) {
        super(view);
    }

    protected Bounds contentArea() {
        return new Bounds(getLeft(), getTop(), getSize().getWidth() - getLeft() - getRight(), getSize().getHeight() - getTop()
                - getBottom());
    }

    @Override
    public View dragFrom(final Location location) {
        location.subtract(getLeft(), getTop());

        return super.dragFrom(location);
    }

    @Override
    public void dragIn(final ContentDrag drag) {
        drag.subtract(getLeft(), getTop());
        super.dragIn(drag);
    }

    @Override
    public void dragOut(final ContentDrag drag) {
        drag.subtract(getLeft(), getTop());
        super.dragOut(drag);
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (overContent(drag.getLocation())) {
            drag.subtract(getLeft(), getTop());
            return super.dragStart(drag);
        } else {
            return null;
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        if (Toolkit.debug) {
            canvas.drawDebugOutline(new Bounds(getSize()), getBaseline(), Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_BORDER));
        }
        int width = getSize().getWidth() - getRight();
        int height = getSize().getHeight() - getBottom();
        Canvas subcanvas = canvas.createSubcanvas(getLeft(), getTop(), width, height);
        wrappedView.draw(subcanvas);
    }

    @Override
    public void drop(final ContentDrag drag) {
        drag.subtract(getLeft(), getTop());
        super.drop(drag);
    }

    @Override
    public void drop(final ViewDrag drag) {
        drag.subtract(getLeft(), getTop());
        super.drop(drag);
    }

    @Override
    public void firstClick(final Click click) {
        if (overContent(click.getLocation())) {
            click.subtract(getLeft(), getTop());
            wrappedView.firstClick(click);
        }
    }

    @Override
    public int getBaseline() {
        return wrappedView.getBaseline() + getTop();
    }

    protected int getBottom() {
        return bottom;
    }

    protected int getLeft() {
        return left;
    }

    @Override
    public Padding getPadding() {
        final Padding padding = wrappedView.getPadding();
        padding.extendTop(getTop());
        padding.extendLeft(getLeft());
        padding.extendBottom(getBottom());
        padding.extendRight(getRight());

        return padding;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        maximumSize.contract(getLeft() + getRight(), getTop() + getBottom());
        final Size size = wrappedView.getRequiredSize(maximumSize);
        size.extend(getLeft() + getRight(), getTop() + getBottom());

        return size;
    }

    protected int getRight() {
        return right;
    }

    @Override
    public Size getSize() {
        final Size size = wrappedView.getSize();
        size.extend(getLeft() + getRight(), getTop() + getBottom());

        return size;
    }

    protected int getTop() {
        return top;
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        super.debugDetails(debug);
        debug.appendln("border", getTop() + "/" + getBottom() + " " + getLeft() + "/" + getRight() + " (top/bottom left/right)");
        debug.appendln("contents", contentArea());
    }

    protected boolean overBorder(final Location location) {
        return !contentArea().contains(location);
    }

    protected boolean overContent(final Location location) {
        return contentArea().contains(location);
    }

    protected boolean isOnBorder() {
        return onBorder;
    }

    @Override
    public View identify(final Location location) {
        getViewManager().getSpy().addTrace(this, "mouse location within border", location);
        getViewManager().getSpy().addTrace(this, "non border area", contentArea());

        if (overBorder(location)) {
            getViewManager().getSpy().addTrace(this, "over border area", contentArea());
            return getView();
        } else {
            location.add(-getLeft(), -getTop());
            return super.identify(location);
        }

    }

    @Override
    public void mouseDown(final Click click) {
        if (overContent(click.getLocation())) {
            click.subtract(getLeft(), getTop());
            wrappedView.mouseDown(click);
        }
    }

    @Override
    public void mouseMoved(final Location at) {
        boolean on = overBorder(at);
        if (onBorder != on) {
            markDamaged();
            onBorder = on;
        }

        if (!on) {
            at.move(-getLeft(), -getTop());
            wrappedView.mouseMoved(at);
        }
    }

    @Override
    public void mouseUp(final Click click) {
        if (overContent(click.getLocation())) {
            click.subtract(getLeft(), getTop());
            wrappedView.mouseUp(click);
        }
    }

    @Override
    public void exited() {
        onBorder = false;
        super.exited();
    }

    @Override
    public View pickupContent(final Location location) {
        location.subtract(getLeft(), getTop());
        return super.pickupContent(location);
    }

    @Override
    public View pickupView(final Location location) {
        if (overBorder(location)) {
            return Toolkit.getViewFactory().createDragViewOutline(getView());
        } else {
            location.subtract(getLeft(), getTop());
            return super.pickupView(location);
        }
    }

    @Override
    public void secondClick(final Click click) {
        if (overContent(click.getLocation())) {
            click.subtract(getLeft(), getTop());
            wrappedView.secondClick(click);
        }
    }

    @Override
    public void setMaximumSize(final Size size) {
        final Size wrappedSize = new Size(size);
        wrappedSize.contract(getLeft() + getRight(), getTop() + getBottom());
        wrappedView.setMaximumSize(wrappedSize);
    }

    @Override
    public void setSize(final Size size) {
        final Size wrappedViewSize = new Size(size);
        wrappedViewSize.contract(getLeft() + getRight(), getTop() + getBottom());
        wrappedView.setSize(wrappedViewSize);
    }

    @Override
    public void setBounds(final Bounds bounds) {
        final Bounds wrappedViewBounds = new Bounds(bounds);
        wrappedViewBounds.contract(getLeft() + getRight(), getTop() + getBottom());
        wrappedView.setBounds(wrappedViewBounds);
    }

    @Override
    public void thirdClick(final Click click) {
        if (overContent(click.getLocation())) {
            click.subtract(getLeft(), getTop());
            wrappedView.thirdClick(click);
        }
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        final Size size = wrappedView.getSize();
        final Bounds bounds = new Bounds(getLeft(), getTop(), size.getWidth(), size.getHeight());

        if (bounds.contains(mouseLocation)) {
            mouseLocation.subtract(getLeft(), getTop());

            return wrappedView.viewAreaType(mouseLocation);
        } else {
            return ViewAreaType.VIEW;
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
