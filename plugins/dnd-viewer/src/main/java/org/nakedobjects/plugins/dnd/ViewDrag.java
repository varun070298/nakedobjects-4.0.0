package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;


/**
 * Details a drag event that affects a view. The target of a ViewDrag is always the workspace of the source
 * view.
 * 
 * <p>
 * An overlay view, as returned by the pickup() method on the source view, is moved by this drag objects so
 * its location follows the pointer by an offset equivalent to the mouse location within the view.
 */
public class ViewDrag extends Drag {
    private final View dragView;
    private Location location;
    /**
     * Offset from the view's top-left corner to the pointer (relative to the view).
     */
    private final Offset overlayOffset;
    private final View view;
    private final View viewsDecoratedWorkspace;
    private final Workspace viewsWorkspace;

    /**
     * Creates a new drag event. The source view has its pickup(), and then, exited() methods called on it.
     * The view returned by the pickup method becomes this event overlay view, which is moved continuously so
     * that it tracks the pointer.
     * 
     * @param view
     *            the view over which the pointer was when this event started
     */
    public ViewDrag(final View view, final Offset offset, final View dragView) {
        this.view = view;
        this.dragView = dragView;
        this.overlayOffset = offset;

        // viewsWorkspace = view.getParent().getWorkspace();
        viewsWorkspace = view.getWorkspace();
        viewsDecoratedWorkspace = viewsWorkspace.getView();
    }

    /**
     * Cancel drag by changing cursor back to pointer.
     */
    @Override
    public void cancel(final Viewer viewer) {
        getSourceView().getFeedbackManager().showDefaultCursor();
    }

    /**
     * Moves the overlay view so it follows the pointer
     */
    protected void drag(final Viewer viewer) {
        if (dragView != null) {
            dragView.markDamaged();
            updateDraggingLocation();
            dragView.markDamaged();
        }
    }

    @Override
    public void drag(final View target, final Location location, final int mods) {
        this.location = location;
        if (dragView != null) {
            dragView.markDamaged();
            updateDraggingLocation();
            dragView.markDamaged();
        }
    }

    /**
     * Ends the drag by calling drop() on the workspace.
     */
    @Override
    public void end(final Viewer viewer) {
        viewer.clearAction();
        viewsDecoratedWorkspace.drop(this);
    }

    @Override
    public View getOverlay() {
        return dragView;
    }

    public Location getLocation() {
        return location;
    }

    public View getSourceView() {
        return view;
    }

    public Location getViewDropLocation() {
        final Location viewLocation = new Location(location);
        viewLocation.subtract(overlayOffset);
        viewLocation.subtract(viewsDecoratedWorkspace.getAbsoluteLocation());
        viewLocation.move(-viewsDecoratedWorkspace.getPadding().getLeft(), -viewsDecoratedWorkspace.getPadding().getTop());
        return viewLocation;
    }

    public void subtract(final Location location) {
        location.subtract(location);
    }

    @Override
    public String toString() {
        return "ViewDrag [" + super.toString() + "]";
    }

    private void updateDraggingLocation() {
        final Location viewLocation = new Location(location);
        viewLocation.subtract(overlayOffset);
        dragView.setLocation(viewLocation);
        dragView.limitBoundsWithin(viewsWorkspace.getSize());
    }

    public void subtract(final int x, final int y) {
        location.subtract(x, y);
    }

}
// Copyright (c) Naked Objects Group Ltd.
