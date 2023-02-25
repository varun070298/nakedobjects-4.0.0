package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;


public class CloseWindowSimpleRender extends SimpleRender implements CloseWindowRender {

    public void draw(Canvas canvas, int width, int height, boolean isDisabled, boolean isOver, boolean isPressed) {
        final int x = 0;
        final int y = 0;

        Color color = color(isDisabled, isOver);
        canvas.drawLine(x + 4, y + 3, x + 10, y + 9, color);
        canvas.drawLine(x + 5, y + 3, x + 11, y + 9, color);
        canvas.drawLine(x + 10, y + 3, x + 4, y + 9, color);
        canvas.drawLine(x + 11, y + 3, x + 5, y + 9, color);
    }
}

// Copyright (c) Naked Objects Group Ltd.
