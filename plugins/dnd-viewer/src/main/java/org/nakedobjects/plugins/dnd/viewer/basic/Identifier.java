package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractViewDecorator;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class Identifier extends AbstractViewDecorator {
    private boolean identified;

    public Identifier(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    public void debugDetails(final DebugString debug) {
        debug.append("Identifier");
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
        final Size s = getSize();
        canvas.drawSolidRectangle(0, 0, s.getWidth(), s.getHeight(), Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));
        wrappedView.draw(canvas);
    }

    @Override
    public void entered() {
        getState().setContentIdentified();
        wrappedView.entered();
        identified = true;
        markDamaged();
    }

    @Override
    public void exited() {
        getState().clearObjectIdentified();
        wrappedView.exited();
        identified = false;
        markDamaged();
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/Identifier [identified=" + identified + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
