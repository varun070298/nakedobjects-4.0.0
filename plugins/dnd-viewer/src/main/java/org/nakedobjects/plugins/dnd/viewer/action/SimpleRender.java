package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;


public class SimpleRender {
    protected Color color(boolean isDisabled, boolean isOver) {
        Color color;
        if (isDisabled) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
        } else {
            color = isOver ? Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2) : Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        }
        return color;
    }

}

// Copyright (c) Naked Objects Group Ltd.
