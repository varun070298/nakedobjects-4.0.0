package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.simple.ObjectView;


class HistogramBar extends ObjectView {

    protected HistogramBar(Content content, ViewSpecification specification, ViewAxis axis) {
        super(content, specification, axis);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int y = 0; // getLocation().getY();
        int height = getSize().getHeight() - 5;
        Text text = Toolkit.getText(ColorsAndFonts.TEXT_LABEL);
        Color color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
        canvas.drawText(getContent().title(), 0, height / 2 + text.getAscent() / 2, color, text);
        
        HistogramAxis axis = (HistogramAxis) getViewAxis();
        double length = (getSize().getWidth() - 160) * axis.getLengthFor(getContent());
        
      ///  double length = 400 * Math.random();
        canvas.drawSolidRectangle(160, y, (int) length, height, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2));
        canvas.drawRectangle(160, y, (int) length, height, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
    }

}

// Copyright (c) Naked Objects Group Ltd.
