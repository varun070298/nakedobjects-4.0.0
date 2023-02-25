package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


public class SimpleStyleWindowBorder  implements BorderDrawing {
    final protected static int LINE_THICKNESS = 5;
    private final static Text TITLE_STYLE = Toolkit.getText(ColorsAndFonts.TEXT_TITLE_SMALL);

    int titlebarHeight = Math.max(WindowControl.HEIGHT + View.VPADDING + TITLE_STYLE.getDescent(), TITLE_STYLE.getTextHeight());
    int baseline = LINE_THICKNESS + WindowControl.HEIGHT;
    int left = LINE_THICKNESS;
    int right = LINE_THICKNESS;
    int top = LINE_THICKNESS + titlebarHeight;
    int bottom = LINE_THICKNESS;

    public void debugDetails(final DebugString debug) {
        debug.append("titlebar " , top - titlebarHeight);
    }

    public void layoutControls(final Size size, WindowControl[] controls) {
        int x = left + View.HPADDING; //size.getWidth() - right - (WindowControl.WIDTH + View.HPADDING) * controls.length;
        final int y = LINE_THICKNESS + View.VPADDING;

        for (int i = 0; i < controls.length; i++) {
            controls[i].setSize(controls[i].getRequiredSize(new Size()));
            controls[i].setLocation(new Location(x, y));
            x += controls[i].getSize().getWidth() + View.HPADDING;
        }
    }

    public void draw(final Canvas canvas, Size s, boolean hasFocus, final ViewState state, WindowControl[] controls, String title) {
        final int x = left;
        final int width = s.getWidth();
        final int height = s.getHeight();

        final Color borderColor = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        canvas.drawRoundedRectangle(0, 0, width, height, 8, 8, borderColor);
        
        final Color titleBarTextColor = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_BLACK) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        final Color titleBarBackgroundColor = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
/*       
        final Color insetColorLight = hasFocus ? Toolkit.getColor("primary2") : Toolkit.getColor("secondary2");
        final Color insetColorDark = hasFocus ? Toolkit.getColor("black") : Toolkit.getColor("black");

        
        // slightly rounded grey border
        canvas.drawRectangle(1, 0, width - 2, height, borderColor);
        canvas.drawRectangle(0, 1, width, height - 2, borderColor);

        for (int i = 2; i < left; i++) {
            canvas.drawRectangle(i, i, width - 2 * i, height - 2 * i, borderColor);
        }

        if (state.isActive()) {
            final int i = left;
            canvas.drawRectangle(i, top, width - 2 * i, height - 2 * i - top, Toolkit.getColor("active"));
        }

*/
        // title bar
        canvas.drawSolidRectangle(left, LINE_THICKNESS, width - left - right, titlebarHeight, titleBarBackgroundColor);
        final int y = LINE_THICKNESS + titlebarHeight - 1;
        canvas.drawLine(x, y, width - right - 1, y, borderColor);
       
        canvas.drawText(title, x + View.HPADDING + (WindowControl.WIDTH + View.HPADDING) * controls.length, baseline, titleBarTextColor, TITLE_STYLE);
/*
        final Color white = Toolkit.getColor("white");
        final int hatchX = View.HPADDING + TITLE_STYLE.stringWidth(title) + 10;
        final int hatchWidth = controls[0].getBounds().getX() - hatchX - 10;
        final int hatchY = LINE_THICKNESS + 2;
        final int hatchHeight = titlebarHeight - 6;
        DrawingUtil.drawHatching(canvas, hatchX, hatchY, hatchWidth, hatchHeight, borderColor, white);
*/
    }

    public void drawTransientMarker(Canvas canvas, Size size) {
        final int height = top - LINE_THICKNESS - 2;
        final int x = size.getWidth() - 50;
        final Image icon = ImageFactory.getInstance().loadIcon("transient", height, null);
        if (icon == null) {
            canvas.drawText("*", x, baseline, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK), Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
        } else {
            canvas.drawImage(icon, x, LINE_THICKNESS + 1, height, height);
            // canvas.drawRectangle(x, LINE_THICKNESS + 1, height, height, Color.RED);
        }

    }

    public void getRequiredSize(Size size, String title, WindowControl[] controls) {
        final int width = left + View.HPADDING + TITLE_STYLE.stringWidth(title) + View.HPADDING + controls.length
                * (WindowControl.WIDTH + View.HPADDING) + View.HPADDING + right;
        size.ensureWidth(width);
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

}

// Copyright (c) Naked Objects Group Ltd.
