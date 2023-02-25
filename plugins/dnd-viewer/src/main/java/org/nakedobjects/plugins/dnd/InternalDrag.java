package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


/**
 * Details a drag event that is internal to view.
 */
public abstract class InternalDrag extends Drag {
    /**
     * Gets the location of the pointer relative to the view.
     */
    public abstract Location getLocation();
}
// Copyright (c) Naked Objects Group Ltd.
