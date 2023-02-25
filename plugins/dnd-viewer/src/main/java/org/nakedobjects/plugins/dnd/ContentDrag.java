package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


/**
 * Details a drag event that affects a view's content (as opposed to the view itself).
 */
public class ContentDrag extends Drag {
    private final View dragView;
    private Location location;
    private View previousTarget;
    private final Content sourceContent;
    private View target;
    private final Workspace workspace;
    private final Location offset;
    private final View source;

    /**
     * Creates a new drag event. The source view has its pickup(), and then, exited() methods called on it.
     * The view returned by the pickup method becomes this event overlay view, which is moved continuously so
     * that it tracks the pointer,
     * 
     * @param source
     *            the view over which the pointer was when this event started
     */
    public ContentDrag(final View source, final Location offset, final View dragView) {
        if (dragView == null) {
            throw new NullPointerException();
        }
        workspace = source.getWorkspace();
        sourceContent = source.getContent();
        this.dragView = dragView;
        this.offset = offset;
        this.source = source.getView();
    }

    /**
     * Cancels drag by calling dragOut() on the current target, and changes the cursor back to the default.
     */
    @Override
    public void cancel(final Viewer viewer) {
        if (target != null) {
            target.dragOut(this);
        }
        viewer.clearAction();
    }

    @Override
    public void drag(final View target, final Location location, final int mods) {
        this.location = location;
        this.target = target;
        this.mods = mods;

        moveDragView();
        crossBoundary(target);
        target.drag(this);
    }

    private void crossBoundary(final View target) {
        if (target != previousTarget) {
            if (previousTarget != null) {
                previousTarget.dragOut(this);
                previousTarget = null;
            }

            target.dragIn(this);
            previousTarget = target;
        }
    }

    private void moveDragView() {
        if (dragView != null) {
            dragView.markDamaged();
            final Location newLocation = new Location(this.location);
            newLocation.subtract(offset);
            dragView.setLocation(newLocation);
            dragView.limitBoundsWithin(workspace.getSize());
            dragView.markDamaged();
        }
    }

    /**
     * Ends the drag by calling drop() on the current target, and changes the cursor back to the default.
     */
    @Override
    public void end(final Viewer viewer) {
        viewer.getSpy().addAction("drop on " + target);
        target.drop(this);
        viewer.clearAction();
    }

    @Override
    public View getOverlay() {
        return dragView;
    }

    public View getSource() {
        return source;
    }

    /**
     * Returns the Content object from the source view.
     */
    public Content getSourceContent() {
        return sourceContent;
    }

    public Location getTargetLocation() {
        final Location location = new Location(this.location);
        location.subtract(target.getAbsoluteLocation());
        // location.add(-getOffset().getX(), -getOffset().getY());
        // location.add(-getOffset().getX(), -getOffset().getY());

        return location;
    }

    public Location getOffset() {
        return offset;
    }

    /**
     * Returns the current target view.
     */
    public View getTargetView() {
        return target;
    }

    @Override
    public String toString() {
        return "ContentDrag [" + super.toString() + "]";
    }

    public void subtract(final int left, final int top) {}
}
// Copyright (c) Naked Objects Group Ltd.
