package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class CalendarBorder extends AbstractBorder {
    private static final int HEIGHT = 20;
    private final CalendarBorderTab[] tabs;
    private int over;
    private int selected;

    protected CalendarBorder(final CalendarTemplate view, final CalendarBorderTab[] tabs) {
        super(view);
        this.tabs = tabs;
        top = HEIGHT;
    }

    @Override
    public void draw(final Canvas canvas) {
        final int width = tabWidth();
        for (int i = 0; i < tabs.length; i++) {
            final Canvas tabcanvas = canvas.createSubcanvas(i * width, 0, width, HEIGHT);
            if (i == selected) {
                tabcanvas.drawSolidRectangle(0, 0, width + 1, HEIGHT + 5, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3));
            } else if (i == over) {
                tabcanvas.drawSolidRectangle(0, 0, width + 1, HEIGHT + 5, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));
            }
            tabs[i].draw(tabcanvas, width);
            tabcanvas.drawRoundedRectangle(0, 0, width + 1, HEIGHT + 5, 5, 5, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
        }
        super.draw(canvas);
    }

    @Override
    public void firstClick(final Click click) {
        if (overContent(click.getLocation())) {
            super.firstClick(click);
        } else {
            final int x = click.getLocation().getX();
            final int i = overTab(x);
            click.subtract(i * tabWidth(), 0);
            if (tabs[i].select(click, (CalendarTemplate) getView())) {
                selected = i;
            }
        }
    }

    private int overTab(final int x) {
        final int width = tabWidth();
        final int i = x / width;
        return i;
    }

    private int tabWidth() {
        return getSize().getWidth() / tabs.length;
    }

    @Override
    public void mouseMoved(final Location at) {
        if (overContent(at)) {
            super.mouseMoved(at);
            over = -1;
            markDamaged();
        } else {
            over = overTab(at.getX());
            markDamaged();
        }
    }

    @Override
    public void exited() {
        super.exited();
        over = -1;
        markDamaged();
    }
}

// Copyright (c) Naked Objects Group Ltd.
