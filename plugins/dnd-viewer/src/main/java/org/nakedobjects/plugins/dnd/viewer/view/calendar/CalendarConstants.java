package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class CalendarConstants {
    public final static String[] days = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
    public final static Color textColor = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
    public final static Color weekendColor = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3);
    public final static Color lineColor = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
    public final static Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);

}

// Copyright (c) Naked Objects Group Ltd.
