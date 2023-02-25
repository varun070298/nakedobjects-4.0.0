package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public interface BorderDrawing {

    public abstract void debugDetails(final DebugString debug);

    public abstract void layoutControls(final Size size, WindowControl[] controls);

    public abstract void draw(
            final Canvas canvas,
            Size s,
            boolean hasFocus,
            final ViewState state,
            WindowControl[] controls,
            String title);

    public abstract void drawTransientMarker(Canvas canvas, Size size);

    public abstract void getRequiredSize(Size size, String title, WindowControl[] controls);

    public abstract int getLeft();

    public abstract int getRight();

    public abstract int getTop();

    public abstract int getBottom();

}

// Copyright (c) Naked Objects Group Ltd.
