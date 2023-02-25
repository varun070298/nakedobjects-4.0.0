package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class NamedCalendarBorderTab implements CalendarBorderTab {
    final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
    final Color color = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
    private final String name;
    private final CalendarDisplay calendarDisplay;

    public NamedCalendarBorderTab(final String name, final CalendarDisplay calendarDisplay) {
        this.name = name;
        this.calendarDisplay = calendarDisplay;
    }

    public boolean select(final Click click, final CalendarTemplate calendar) {
        calendar.setCalendarDisplay(calendarDisplay);
        return true;
    }

    public void draw(final Canvas tabcanvas, final int width) {
        tabcanvas.drawText(name, 5, 15, color, style);
    }
}

// Copyright (c) Naked Objects Group Ltd.
