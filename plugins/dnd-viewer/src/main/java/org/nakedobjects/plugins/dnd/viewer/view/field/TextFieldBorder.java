package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;


/**
 * Border decorator to draw a white background and 3D style border around a text field.
 */
public class TextFieldBorder extends AbstractBorder {

    public TextFieldBorder(final View view) {
        super(view);
        top = bottom = left = right = 2;
    }

    @Override
    public void draw(final Canvas canvas) {
        final int height = getSize().getHeight() - 2;
        final int width = getSize().getWidth();
        canvas.drawSolidRectangle(0, 1, width - 1, height - 2, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        canvas.drawRectangle(0, 1, width - 3, height - 2, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1));
        canvas.drawRectangle(1, 2, width - 1, height - 2, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));

        super.draw(canvas);
    }
}
// Copyright (c) Naked Objects Group Ltd.
