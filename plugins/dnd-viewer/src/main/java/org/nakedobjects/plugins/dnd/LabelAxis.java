package org.nakedobjects.plugins.dnd;

public class LabelAxis implements ViewAxis {
    private int width;

    public LabelAxis() {}

    public void accommodateWidth(final int width) {
        this.width = Math.max(this.width, width);
    }

    public int getWidth() {
        return width;
    }
}
// Copyright (c) Naked Objects Group Ltd.
