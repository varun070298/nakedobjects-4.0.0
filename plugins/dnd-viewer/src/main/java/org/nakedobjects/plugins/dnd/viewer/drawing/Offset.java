package org.nakedobjects.plugins.dnd.viewer.drawing;

public class Offset {

    private int dx;
    private int dy;

    public Offset(final Location locationInViewer, final Location locationInView) {
        dx = locationInViewer.getX() - locationInView.getX();
        dy = locationInViewer.getY() - locationInView.getY();
    }

    public Offset(final int dx, final int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Offset(final Location location) {
        this.dx = location.getX();
        this.dy = location.getY();
    }

    public int getDeltaX() {
        return dx;
    }

    public int getDeltaY() {
        return dy;
    }

    public Location offset(final Location locationInViewer) {
        final Location location = new Location(locationInViewer);
        location.move(dx, dy);
        return location;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Offset) {
            Offset offset;
            offset = (Offset) obj;
            return offset.dx == dx && offset.dy == dy;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Offset " + dx + ", " + dy;
    }

    public void add(final int dx, final int dy) {
        this.dx += dx;
        this.dy += dy;
    }

    public void subtract(final int dx, final int dy) {
        add(-dx, -dy);
    }
}
// Copyright (c) Naked Objects Group Ltd.
