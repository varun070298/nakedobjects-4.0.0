package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;

public class Button3DStyleRender implements ButtonRender {
    private static final int TEXT_PADDING = 12;
    private static final Text style = Toolkit.getText(ColorsAndFonts.TEXT_CONTROL);
    private final int buttonHeight;

    public Button3DStyleRender() {     
        this.buttonHeight = 4 + style.getTextHeight() + 4;
    }
    
    public void draw(Canvas canvas, Size size, boolean isDisabled, boolean isDefault, boolean hasFocus, boolean isOver, boolean isPressed, String text) {
        final int x = 0;
        final int y = 0;
        
        final int buttonWidth = TEXT_PADDING + style.stringWidth(text) + TEXT_PADDING;

        canvas.drawSolidRectangle(x, y, buttonWidth, buttonHeight,  Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));

        final Color color = isDisabled ? Toolkit.getColor(ColorsAndFonts.COLOR_MENU_DISABLED) : Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        final Color border = isDisabled ? Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        canvas.drawRectangle(x, y, buttonWidth, buttonHeight, isOver & !isDisabled ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1) : Toolkit
                .getColor(ColorsAndFonts.COLOR_BLACK));
        canvas.draw3DRectangle(x + 1, y + 1, buttonWidth - 2, buttonHeight - 2, border, !isPressed);
        canvas.draw3DRectangle(x + 2, y + 2, buttonWidth - 4, buttonHeight - 4, border, !isPressed);
        if (isDefault) {
            canvas.drawRectangle(x + 3, y + 3, buttonWidth - 6, buttonHeight - 6, border);
        }
        if (hasFocus) {
            canvas.drawRectangle(x + 3, y + 3, buttonWidth - 6, buttonHeight - 6, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        }
        canvas.drawText(text, x + TEXT_PADDING, y + buttonHeight / 2 + style.getMidPoint(), color, style);
    }

    public Size getMaximumSize(String text) {
        final int buttonWidth = TEXT_PADDING + Toolkit.getText(ColorsAndFonts.TEXT_CONTROL).stringWidth(text) + TEXT_PADDING;
        return new Size(buttonWidth, buttonHeight);
    }
}


// Copyright (c) Naked Objects Group Ltd.
