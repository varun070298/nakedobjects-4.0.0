package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class TextFieldResizeBorder extends ResizeBorder {
    public static final int BORDER_WIDTH = NakedObjectsContext.getConfiguration().getInteger(
            Properties.PROPERTY_BASE + "field-resize-border", 5);

    public TextFieldResizeBorder(final View view) {
        super(view, RIGHT + DOWN, 1, 1);
    }

    @Override
    protected void drawResizeBorder(final Canvas canvas, final Size size) {
        if (resizing) {
            final Shape shape = new Shape(0, 0);
            int resizeMarkerSize = 10;
            shape.extendsLine(resizeMarkerSize, 0);
            shape.extendsLine(0, resizeMarkerSize);
            shape.extendsLine(-resizeMarkerSize, -resizeMarkerSize);
            Color color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
            int height = size.getHeight();
            int width = size.getWidth();
            canvas.drawSolidShape(shape, width - resizeMarkerSize, height, color);
            canvas.drawRectangle(0, 0, width, height, color);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
