package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;


public class DebugDrawingAbsolute implements DebugInfo {
    private final View view;

    public DebugDrawingAbsolute(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        view.draw(new DebugCanvasAbsolute(debug, new Bounds(view.getAbsoluteLocation(), view.getSize())));
    }

    public String debugTitle() {
        return "Drawing (Absolute)";
    }
}
// Copyright (c) Naked Objects Group Ltd.
