package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;


public class IconizeWindowSimpleRender extends SimpleRender implements IconizeWindowRender {

    public void draw(Canvas canvas, int width, int height, boolean isDisabled, boolean isOver, boolean isPressed) {
        final int x = 0;
        final int y = 0;

        Color color = color(isDisabled, isOver);
        canvas.drawLine(x + 3, y + 8, x + 8, y + 8, color);
        canvas.drawLine(x + 3, y + 9, x + 8, y + 9, color);
    }
}

// Copyright (c) Naked Objects Group Ltd.
