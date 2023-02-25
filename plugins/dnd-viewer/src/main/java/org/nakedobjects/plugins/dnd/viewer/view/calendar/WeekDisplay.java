package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import java.util.Calendar;
import java.util.Date;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class WeekDisplay implements CalendarDisplay {

    public void draw(final Canvas canvas, final Size size) {
        weekGrid(canvas, size);
    }

    public boolean drop(final ContentDrag drag, final Size size) {
        /*
         * int day = dayAtPointerForMonthView(drag);
         * 
         * Naked adapter = drag.getSourceContent().getNaked(); // booking.setDate(new Date(2006, 11, day));
         * 
         * return day > 0;
         */
        return false;
    }

    private void weekGrid(final Canvas canvas, final Size size) {
        final int width = size.getWidth() / 7;
        final int height = size.getHeight();

        final int y = CalendarConstants.style.getTextHeight();
        canvas.drawSolidRectangle(width * 5, 0, width * 2, height, CalendarConstants.weekendColor);
        canvas.drawLine(0, y + 4, width * 7, y + 4, CalendarConstants.lineColor);

        for (int i = 0; i < 7; i++) {
            int x = i * width;
            canvas.drawLine(x, 0, x, height, CalendarConstants.lineColor);
            final String day = CalendarConstants.days[i];
            x = x + width / 2 - CalendarConstants.style.stringWidth(day) / 2;
            canvas.drawText(day, x + 5, y, CalendarConstants.textColor, CalendarConstants.style);
        }
    }

    public void firstClick(final Click click, final Size size) {}

    public Size getBlockSize(final Size size) {
        return new Size(size.getWidth() / 7, size.getHeight());
    }

    public void layoutDate(final View v, final Date date, final int width, final int height) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        final int col = (day - 1) % 7;
        final int x = width * col;
        v.setLocation(new Location(x, 30));
        v.setSize(new Size(width, height));
    }

    public Size getRequiredSize() {
        return new Size(80 * 7, 160);
    }

}

// Copyright (c) Naked Objects Group Ltd.
