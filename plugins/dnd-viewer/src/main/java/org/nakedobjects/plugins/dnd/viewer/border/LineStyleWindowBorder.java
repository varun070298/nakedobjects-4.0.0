package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;

public class LineStyleWindowBorder implements BorderDrawing {
    private final static Text TITLE_STYLE = Toolkit.getText(ColorsAndFonts.TEXT_TITLE_SMALL);

    private int titlebarHeight = Math.max(WindowControl.HEIGHT + View.VPADDING + TITLE_STYLE.getDescent(), TITLE_STYLE.getTextHeight());
    
    public void debugDetails(DebugString debug) {}

    public void draw(Canvas canvas, Size s, boolean hasFocus, ViewState state, WindowControl[] controls, String title) {
        final Color borderColor = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_BLACK) : Toolkit
                .getColor(ColorsAndFonts.COLOR_SECONDARY1);
        canvas.drawRectangle(0, 0, s.getWidth(), s.getHeight(), borderColor);
        canvas.drawLine(0, titlebarHeight, s.getWidth(), titlebarHeight, borderColor);
        canvas.drawText(title, 6, TITLE_STYLE.getLineHeight(), borderColor, Toolkit.getText(ColorsAndFonts.TEXT_TITLE_SMALL));
    }

    // TODO transiency should be flagged elsewhere and dealt with in the draw method.
    public void drawTransientMarker(Canvas canvas, Size size) {}

    public int getBottom() {
        return 7;
    }

    public int getLeft() {
        return 5;
    }

    public void getRequiredSize(Size size, String title, WindowControl[] controls) {}

    public int getRight() {
        return 5;
    }

    public int getTop() {
        return titlebarHeight + 5;
    }

    public void layoutControls(Size size, WindowControl[] controls) {
        int x = size.getWidth() - 1 - (WindowControl.WIDTH + View.HPADDING) * controls.length; 
        final int y = 2 + View.VPADDING;
        for (int i = 0; i < controls.length; i++) {
            controls[i].setSize(controls[i].getRequiredSize(new Size()));
            controls[i].setLocation(new Location(x, y));
            x += controls[i].getSize().getWidth() + View.HPADDING;
        }
    }

}


// Copyright (c) Naked Objects Group Ltd.
