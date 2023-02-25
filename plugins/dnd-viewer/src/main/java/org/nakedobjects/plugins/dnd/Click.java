package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;


/**
 * Describes a mouse click event.
 */
public class Click extends PointerEvent {
    private final Location location;
    private final Location locationWithinViewer;

    /**
     * Creates a new click event object.
     * 
     * @param source
     *            the view over which the pointer was when this click occurred
     * @param mouseLocation
     *            the location of the mouse relative to the viewer
     * @param modifiers
     *            the button and key held down during the click (@see java.awt.event.MouseEvent)
     */
    public Click(final View source, final Location mouseLocation, final int modifiers) {
        super(modifiers);

        this.location = new Location(mouseLocation);
        this.locationWithinViewer = new Location(mouseLocation);
    }

    public Location getLocation() {
        return location;
    }

    public Location getLocationWithinViewer() {
        return locationWithinViewer;
    }

    /**
     * Translate the location of this event by the specified offset.
     */
    public void subtract(final int x, final int y) {
        location.subtract(x, y);
    }

    @Override
    public String toString() {
        return "Click [location=" + location + "," + super.toString() + "]";
    }

    public void add(final Offset offset) {
        location.add(offset.getDeltaX(), offset.getDeltaY());
    }

    public void subtract(final Offset offset) {
        subtract(offset.getDeltaX(), offset.getDeltaY());
    }

    public void subtract(final Location location) {
        subtract(location.getX(), location.getY());
    }
}
// Copyright (c) Naked Objects Group Ltd.
