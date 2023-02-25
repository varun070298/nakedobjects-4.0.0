package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;


public class SimpleInternalDrag extends InternalDrag {
    private final Location location;
    // TODO replace Location with Offset
    private final Location offset;
    private final View view;

    /**
     * Creates a new drag event. The source view has its pickup(), and then, exited() methods called on it.
     * The view returned by the pickup method becomes this event overlay view, which is moved continuously so
     * that it tracks the pointer,
     * 
     * @param view
     *            the view over which the pointer was when this event started
     * @param location
     *            the location within the viewer (the Frame/Applet/Window etc)
     * 
     * TODO combine the two constructors
     */
    public SimpleInternalDrag(final View view, final Location location) {
        this.view = view;

        this.location = new Location(location);
        offset = view.getAbsoluteLocation();

        final Padding targetPadding = view.getPadding();
        final Padding containerPadding = view.getView().getPadding();
        offset.add(containerPadding.getLeft() - targetPadding.getLeft(), containerPadding.getTop() - targetPadding.getTop());

        this.location.subtract(offset);
    }

    public SimpleInternalDrag(final View view, final Offset off) {
        this.view = view;

        location = new Location();

        offset = new Location(off.getDeltaX(), off.getDeltaY());

        final Padding targetPadding = view.getPadding();
        final Padding containerPadding = view.getView().getPadding();
        offset.add(containerPadding.getLeft() - targetPadding.getLeft(), containerPadding.getTop() - targetPadding.getTop());

        this.location.subtract(offset);
    }

    @Override
    public void cancel(final Viewer viewer) {
        view.dragCancel(this);
    }

    @Override
    public void drag(final View target, final Location location, final int mods) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.subtract(offset);
        view.drag(this);
    }

    @Override
    public void end(final Viewer viewer) {
        view.dragTo(this);
    }

    /**
     * Gets the location of the pointer relative to the view.
     */
    @Override
    public Location getLocation() {
        return new Location(location);
    }

    @Override
    public View getOverlay() {
        return null;
    }

    @Override
    public String toString() {
        final ToString s = new ToString(this, super.toString());
        s.append("location", location);
        s.append("relative", getLocation());
        return s.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
