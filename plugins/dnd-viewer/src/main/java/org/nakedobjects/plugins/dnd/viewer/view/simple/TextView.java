package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;


public class TextView extends AbstractView {
    private final Text style = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
    private final Color color = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
    private final String text;

    public TextView(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        final NakedObject object = content.getNaked();
        text = object == null ? "" : object.titleString();
    }

    @Override
    public boolean canFocus() {
        return false;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawText(text, HPADDING, getBaseline(), color, style);
    }

    @Override
    public int getBaseline() {
        return style.getAscent() + VPADDING;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final int width = style.stringWidth(text) + HPADDING * 2;
        final int height = style.getTextHeight() + VPADDING * 2;
        return new Size(width, height);
    }
}
// Copyright (c) Naked Objects Group Ltd.
