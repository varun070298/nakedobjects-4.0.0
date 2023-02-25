package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


/**
 * Details a drag event - from drag start to drop,
 */
public abstract class Drag extends PointerEvent {
    protected Drag() {
        super(0);
    }

    /**
     * Indicates the drag has been cancelled; no action should be taken.
     */
    public abstract void cancel(final Viewer viewer);

    /**
     * Indicates that the drag state has changed.
     */
    public abstract void drag(final View target, final Location location, final int mods);

    /**
     * Indicates the drag has properly ended (the mouse button has been released)
     * 
     */
    public abstract void end(final Viewer viewer);

    public abstract View getOverlay();
}
// Copyright (c) Naked Objects Group Ltd.
