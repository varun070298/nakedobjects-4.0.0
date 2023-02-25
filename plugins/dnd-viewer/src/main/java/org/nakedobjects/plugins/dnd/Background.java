package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


/**
 * A strategy for rendering the background of the application window.
 */
public interface Background {
    void draw(Canvas canvas, Size size);
}
// Copyright (c) Naked Objects Group Ltd.
