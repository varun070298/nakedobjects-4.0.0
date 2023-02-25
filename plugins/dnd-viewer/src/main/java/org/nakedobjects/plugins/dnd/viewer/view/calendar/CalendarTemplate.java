package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractViewDecorator;


public class CalendarTemplate extends AbstractViewDecorator {
    private CalendarDisplay calendarDisplay;

    protected CalendarTemplate(final View wrappedView, final CalendarDisplay initialCalendarDisplay) {
        super(wrappedView);
        setCalendarDisplay(initialCalendarDisplay);
    }

    @Override
    public void draw(final Canvas canvas) {
        calendarDisplay.draw(canvas, getSize());
        super.draw(canvas);
    }

    @Override
    public void drop(final ContentDrag drag) {
        if (calendarDisplay.drop(drag, getSize())) {
            invalidateLayout();
        } else {
            super.drop(drag);
        }
    }

    /*
     * public void drag(InternalDrag drag) { int day = dayAtPointerForMonthView(drag); if(day != overDay) {
     * overDay = day; markDamaged(); }
     * 
     * super.drag(drag); }
     * 
     * public void dragCancel(InternalDrag drag) { super.dragCancel(drag); overDay = -1; }
     * 
     * public void dragTo(InternalDrag drag) { super.dragTo(drag); overDay = -1; }
     */

    @Override
    public void firstClick(final Click click) {
        calendarDisplay.firstClick(click, getSize());
        // super.firstClick(click);
    }

    public void setCalendarDisplay(final CalendarDisplay calendarDisplay) {
        this.calendarDisplay = calendarDisplay;
        ((CalendarAxis) wrappedView.getViewAxis()).setCalendarDisplay(calendarDisplay);

    }
}

// Copyright (c) Naked Objects Group Ltd.
