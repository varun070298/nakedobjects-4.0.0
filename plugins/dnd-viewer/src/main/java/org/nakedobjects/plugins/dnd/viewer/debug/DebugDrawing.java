package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;


public class DebugDrawing implements DebugInfo {
    private final View view;

    public DebugDrawing(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        view.draw(new DebugCanvas(debug, new Bounds(view.getBounds())));
    }

    public String debugTitle() {
        return "Drawing";
    }
}
// Copyright (c) Naked Objects Group Ltd.
