package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractViewDecorator;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class SimpleIdentifier extends AbstractViewDecorator {

    public SimpleIdentifier(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    public void debugDetails(final DebugString debug) {
        debug.append("SimpleIdentifier");
    }

    @Override
    public void dragIn(final ContentDrag drag) {
        wrappedView.dragIn(drag);
        markDamaged();
    }

    @Override
    public void dragOut(final ContentDrag drag) {
        wrappedView.dragOut(drag);
        markDamaged();
    }

    @Override
    public void draw(final Canvas canvas) {
        Color color = null;
        if (getState().canDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_VALID);
        } else if (getState().cantDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_INVALID);
        } else if (getState().isViewIdentified() || getState().isObjectIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
        }

        wrappedView.draw(canvas.createSubcanvas());

        if (color != null) {
            final Size s = getSize();
            canvas.drawRectangle(0, 0, s.getWidth() - 1, s.getHeight() - 1, color);
            canvas.drawRectangle(1, 1, s.getWidth() - 3, s.getHeight() - 3, color);
        }
    }

    @Override
    public void entered() {
        getState().setContentIdentified();
        wrappedView.entered();
        markDamaged();
    }

    @Override
    public void exited() {
        getState().clearObjectIdentified();
        wrappedView.exited();
        markDamaged();
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/SimpleIdentifier";
    }
}
// Copyright (c) Naked Objects Group Ltd.
