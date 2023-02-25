package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class DebugBorder extends AbstractBorder {
    public DebugBorder(final View wrappedView) {
        super(wrappedView);

        bottom = Toolkit.getText(ColorsAndFonts.TEXT_DEBUG).getTextHeight();
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        debug.append("DebugBorder");
    }

    @Override
    public void draw(final Canvas canvas) {
        final String debug = getView() + " " + getState();
        Text text = Toolkit.getText(ColorsAndFonts.TEXT_DEBUG);
        final int baseline = wrappedView.getSize().getHeight() + text.getAscent();
        final Color color = Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BASELINE);
        canvas.drawText(debug, 0, baseline, color, text);

        super.draw(canvas);
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/DebugBorder";
    }

    @Override
    public void firstClick(final Click click) {
        new DebugOption().execute(getWorkspace(), getView(), click.getLocation());
    }
}
// Copyright (c) Naked Objects Group Ltd.
