package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;


public class ResizeWindowSimpleRender extends SimpleRender implements ResizeWindowRender {

    public void draw(Canvas canvas, int width, int height, boolean isDisabled, boolean isOver, boolean isPressed) {
        final int x = 0;
        final int y = 0;

        Color color = color(isDisabled, isOver);
        canvas.drawRectangle(x + 3, y + 2, 8, 8, color);
        canvas.drawLine(x + 3, y + 3, x + 10, y + 3, color);
    }

}

// Copyright (c) Naked Objects Group Ltd.
