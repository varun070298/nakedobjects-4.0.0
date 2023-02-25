package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class ButtonLineStyleRender implements ButtonRender {
    private static final int TEXT_PADDING = 12;
    private static final Text style = Toolkit.getText(ColorsAndFonts.TEXT_CONTROL);
    private final int buttonHeight;

    public ButtonLineStyleRender() {
        this.buttonHeight = 2 + style.getTextHeight() + 2;
    }

    public void draw(
            Canvas canvas,
            Size size,
            boolean isDisabled,
            boolean isDefault,
            boolean hasFocus,
            boolean isOver,
            boolean isPressed,
            String text) {
        final int buttonWidth = TEXT_PADDING + style.stringWidth(text) + TEXT_PADDING;

        final Color borderColor;
        Color textColor = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        if (isDisabled) {
            borderColor = textColor = Toolkit.getColor(ColorsAndFonts.COLOR_MENU_DISABLED);
        } else if (isDefault) {
            borderColor = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
        } else if (isOver || hasFocus) {
            borderColor = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        } else {
            borderColor = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        }
        canvas.drawRoundedRectangle(0, 0, buttonWidth, buttonHeight, 0, 0, borderColor);
        canvas.drawText(text, TEXT_PADDING, buttonHeight / 2 + style.getMidPoint(), textColor, style);
    }

    public Size getMaximumSize(String text) {
        final int buttonWidth = TEXT_PADDING + Toolkit.getText(ColorsAndFonts.TEXT_CONTROL).stringWidth(text) + TEXT_PADDING;
        return new Size(buttonWidth, buttonHeight);
    }
}

// Copyright (c) Naked Objects Group Ltd.
