package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;


public interface CalendarBorderTab {

    void draw(Canvas tabcanvas, int width);

    boolean select(Click click, CalendarTemplate calendarTemplate);

}

// Copyright (c) Naked Objects Group Ltd.
