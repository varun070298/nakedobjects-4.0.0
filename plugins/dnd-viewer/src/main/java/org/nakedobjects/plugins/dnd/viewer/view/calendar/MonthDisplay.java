package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import java.util.Calendar;
import java.util.Date;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class MonthDisplay implements CalendarDisplay {
    private int overDay;
    private final Calendar focusDate;

    public MonthDisplay() {
        focusDate = Calendar.getInstance();
    }

    public void draw(final Canvas canvas, final Size size) {
        monthGrid(canvas, size);
    }

    public boolean drop(final ContentDrag drag, final Size size) {
        final int day = dayAtPointerForMonthView(drag, size);

        final NakedObject adapter = drag.getSourceContent().getNaked();
        // booking.setDate(new Date(2006, 11, day));

        return day > 0;
    }

    private int dayAtPointerForMonthView(final ContentDrag drag, final Size size) {
        final int width = size.getWidth() / 7;
        final int height = size.getHeight() / 5;

        final Location pointer = drag.getTargetLocation();
        final int column = pointer.getX() / width;
        final int row = pointer.getY() / height;

        final int day = row * 7 + column + 1;

        System.out.println(pointer + " " + day);
        return day;
    }

    private void monthGrid(final Canvas canvas, final Size size) {
        final int width = size.getWidth() / 7;
        final int height = size.getHeight() / 5;

        canvas.drawSolidRectangle(0, 0, width * 2, height * 5, CalendarConstants.weekendColor);
        int day = 1 - getMonthOffSet();
        int y = 4;
        for (int rows = 0; rows < 5; rows++) {
            canvas.drawLine(0, rows * height, width * 7, rows * height, CalendarConstants.lineColor);
            int x = 4;
            for (int cols = 0; cols < 7; cols++) {
                if (day > 0) {
                    canvas.drawText("" + day, x, y + CalendarConstants.style.getAscent(), CalendarConstants.textColor,
                            CalendarConstants.style);
                }
                day++;
                x += width;
            }
            y += height;
        }
        for (int cols = 0; cols < 7; cols++) {
            canvas.drawLine(cols * width, 0, cols * width, height * 5, CalendarConstants.lineColor);
        }

        if (overDay > 0) {
            y = overDay / 7 * height;
            final int x = (overDay % 7) * width;
            canvas.drawRectangle(x, y, width, height, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1));
        }
    }

    public void firstClick(final Click click, final Size size) {
        if (new Bounds(0, 0, 10, 10).contains(click.getLocation())) {
            focusDate.add(Calendar.MONTH, -1);
        } else if (new Bounds(size.getWidth() - 10, size.getHeight() - 10, 10, 10).contains(click.getLocation())) {
            focusDate.add(Calendar.MONTH, 1);
        }
    }

    public Size getBlockSize(final Size size) {
        return new Size(size.getWidth() / 7, size.getHeight() / 5);
    }

    public void layoutDate(final View v, final Date date, final int width, final int height) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        final int col = (day - 1) % 7;
        final int row = (day - 1) / 7;

        final int x = width * col + 20;
        final int y = height * row + 5;
        v.layout(new Size(width - 19, height - 6));
        v.setLocation(new Location(x, y));
        v.setSize(new Size(width - 19, height - 6));
    }

    private int getMonthOffSet() {
        final Calendar c = Calendar.getInstance();
        c.set(focusDate.get(Calendar.YEAR), focusDate.get(Calendar.MONTH), 1);
        final int offset = c.get(Calendar.DAY_OF_WEEK);
        return offset;
    }

    public Size getRequiredSize() {
        return new Size(80 * 7, 250);
    }

}

// Copyright (c) Naked Objects Group Ltd.
