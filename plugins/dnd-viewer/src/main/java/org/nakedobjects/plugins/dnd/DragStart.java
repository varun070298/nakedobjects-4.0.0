package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;


public class DragStart extends PointerEvent {
    private final Location location;

    public DragStart(final Location location, final int mods) {
        super(mods);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void subtract(final Location location) {
        this.location.subtract(location);
    }

    public void subtract(final int x, final int y) {
        location.subtract(x, y);
    }

    public void add(final Offset offset) {
        location.add(offset.getDeltaX(), offset.getDeltaY());
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("location", location);
        str.append("buttons", super.toString());
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
