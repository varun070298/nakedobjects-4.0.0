package org.nakedobjects.viewer.dnd.example;

import org.nakedobjects.nof.core.util.AsString;
import org.nakedobjects.viewer.dnd.Canvas;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.Toolkit;
import org.nakedobjects.viewer.dnd.drawing.Color;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.view.simple.AbstractView;


public abstract class DrawingView extends AbstractView {
    private Size requiredSize;

    public DrawingView(final Content content) {
        super(content, null, null);
    }

    public void draw(final Canvas canvas) {
        final int width = requiredSize.getWidth();
        final int height = requiredSize.getHeight();
        final int left = 0, top = 0;
        final int right = 10 + width - 1 + 10;
        final int bottom = 10 + height - 1 + 10;

        Color gray = Toolkit.getColor(0xcccccc);
        // horizontal lines
        canvas.drawLine(left, top + 10, right, top + 10, gray);
        canvas.drawLine(left, bottom - 10, right, bottom - 10, gray);

        // vertical lines
        canvas.drawLine(left + 10, top, left + 10, bottom, gray);
        canvas.drawLine(right - 10, top, right - 10, bottom, gray);

        canvas.drawRectangle(left + 10, top + 10, width - 1, height - 1, Toolkit.getColor(0xeeeeee));

        draw(canvas, left + 10, top + 10);
    }

    protected abstract void draw(final Canvas canvas, final int x, final int y);

    public Size getRequiredSize(final Size maximumSize) {
        Size s = new Size(requiredSize);
        s.extend(20, 20);
        return s;
    }

    public void setMaximumSize(final Size size) {
        this.requiredSize = size;
    }

    public String toString() {
        AsString ts = new AsString(this);
        ts.append("size", requiredSize);
        toString(ts);
        return ts.toString();
    }

    protected abstract void toString(final AsString ts);
}
// Copyright (c) Naked Objects Group Ltd.
