package org.nakedobjects.plugins.dnd.viewer.drawing;

public class Location {
    int x;
    int y;

    public Location() {
        x = 0;
        y = 0;
    }

    public Location(final int x, final int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Location(final Location location) {
        x = location.x;
        y = location.y;
    }

    public void add(final int x, final int y) {
        move(x, y);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Location) {
            final Location object = (Location) obj;

            return object.x == this.x && object.y == this.y;
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(final int dx, final int dy) {
        x += dx;
        y += dy;
    }

    public Offset offsetFrom(final Location location) {

        Offset offset;
        offset = new Offset(x - location.x, y - location.y);
        return offset;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public void subtract(final int x, final int y) {
        move(-x, -y);
    }

    public void subtract(final Location location) {
        move(-location.x, -location.y);
    }

    public void subtract(final Offset offset) {
        move(-offset.getDeltaX(), -offset.getDeltaY());
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public void translate(final Location offset) {
        move(offset.x, offset.y);
    }

    public void translate(final Offset offset) {
        move(offset.getDeltaX(), offset.getDeltaY());
    }
}
// Copyright (c) Naked Objects Group Ltd.
