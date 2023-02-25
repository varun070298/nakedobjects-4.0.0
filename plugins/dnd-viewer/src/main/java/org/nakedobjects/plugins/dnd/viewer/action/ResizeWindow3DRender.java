package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;

public class ResizeWindow3DRender implements ResizeWindowRender {

    public void draw(Canvas canvas, int width, int height, boolean isDisabled, boolean isOver, boolean isPressed) {
        final int x = 0;
        final int y = 0;

        canvas.drawRectangle(x + 1, y + 1, width - 1, height - 1, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        canvas.drawRectangle(x, y, width - 1, height - 1, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1));
        canvas.drawRectangle(x + 3, y + 2, 8, 8, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
        canvas.drawLine(x + 3, y + 3, x + 10, y + 3, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
    }

}


// Copyright (c) Naked Objects Group Ltd.
