package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import org.nakedobjects.plugins.dnd.ViewAxis;


public class CalendarAxis implements ViewAxis {
    private CalendarDisplay calendarDisplay;

    public void setCalendarDisplay(final CalendarDisplay calendarDisplay) {
        this.calendarDisplay = calendarDisplay;
    }

    public CalendarDisplay getCalendarDisplay() {
        return calendarDisplay;
    }

}

// Copyright (c) Naked Objects Group Ltd.
