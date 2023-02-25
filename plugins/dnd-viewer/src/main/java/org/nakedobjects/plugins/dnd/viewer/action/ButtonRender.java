package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public interface ButtonRender {

    void draw(Canvas canvas, Size size, boolean isDisabled, boolean isDefault, boolean hasFocus, boolean isOver, boolean isPressed, String text);

    Size getMaximumSize(String text);

}


// Copyright (c) Naked Objects Group Ltd.
