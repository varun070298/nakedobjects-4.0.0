package org.nakedobjects.viewer.dnd.example;

import org.nakedobjects.viewer.dnd.Canvas;
import org.nakedobjects.viewer.dnd.Toolkit;
import org.nakedobjects.viewer.dnd.view.simple.AbstractView;


class TestCanvasView2 extends AbstractView {
    
    public void draw(final Canvas canvas) {
        canvas.clearBackground(this, Toolkit.getColor(0xfffff));

        int canvasWidth = getSize().getWidth();
        int canvasHeight = getSize().getHeight();

        canvas.drawRectangleAround(this, Toolkit.getColor(0xff0000));
        canvas.drawRectangle(1, 1, canvasWidth - 2, canvasHeight - 2, Toolkit.getColor(0xdddddd));
        canvas.drawSolidRectangle(2, 2, canvasWidth - 4, canvasHeight - 4, Toolkit.getColor(0x00ff00));

    }
}
// Copyright (c) Naked Objects Group Ltd.
