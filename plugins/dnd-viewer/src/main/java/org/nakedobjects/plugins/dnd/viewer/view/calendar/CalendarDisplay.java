package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import java.util.Date;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public interface CalendarDisplay {
    void draw(Canvas canvas, Size size);

    boolean drop(ContentDrag drag, Size size);

    void firstClick(Click click, Size size);

    Size getBlockSize(Size size);

    void layoutDate(View v, Date date, int width, int height);

    Size getRequiredSize();
}

// Copyright (c) Naked Objects Group Ltd.
