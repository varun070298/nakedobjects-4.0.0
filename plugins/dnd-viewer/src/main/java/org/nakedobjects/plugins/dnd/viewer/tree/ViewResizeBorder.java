package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.basic.ResizeBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.DrawingUtil;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;

// TODO enhance so the direction of resizing can be specified (could limit to width on right, height on bottom, or width/height from corner

public class ViewResizeBorder extends ResizeBorder {
    public static final int BORDER_WIDTH = NakedObjectsContext.getConfiguration().getInteger(
            Properties.PROPERTY_BASE + "tree-resize-border", 7);

    public ViewResizeBorder(final View view) {
        super(view, RIGHT, BORDER_WIDTH, 2);
    }

    @Override
    protected void drawResizeBorder(final Canvas canvas, final Size size) {
        final int x = getSize().getWidth() - BORDER_WIDTH;
        final int height = getSize().getHeight() - 1;
        int h1 = 30;
        int h2 = 10;
        if (height < h1 + h2 * 2) {
            h1 = Math.min(0, height - h2 * 2);
        } else {
            h2 = (height - h1) / 2;
        }

        final boolean hasFocus = getParent().containsFocus();
        final Color borderColor = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
        canvas.drawSolidRectangle(x, 0, BORDER_WIDTH, height, borderColor);

        Color secondary2 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        canvas.drawRectangle(x, -1, BORDER_WIDTH, height + 2, secondary2);

        final Color color = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2) : secondary2;
        DrawingUtil.drawHatching(canvas, x + 1, h2, BORDER_WIDTH - 2, h1, color, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
    }

}
// Copyright (c) Naked Objects Group Ltd.
