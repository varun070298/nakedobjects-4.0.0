package org.nakedobjects.plugins.dnd.viewer.view.specification;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class ServiceBorder extends AbstractBorder {
    private static final int BORDER = 13;

    public ServiceBorder(final int size, final View wrappedView) {
        super(wrappedView);

        top = size;
        left = size;
        bottom = size;
        right = size + BORDER;
    }

    public ServiceBorder(final View wrappedView) {
        this(1, wrappedView);
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        debug.append("ServiceBorder " + top + " pixels");
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        Color color = null;
        final ViewState state = getState();
        final boolean hasFocus = getViewManager().hasFocus(getView());
        if (hasFocus) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);
        } else if (state.isObjectIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        }

        final Size s = getSize();
        if (color != null) {
            if (hasFocus) {
                final int xExtent = s.getWidth() - left;
                for (int i = 0; i < left; i++) {
                    canvas.drawRectangle(i, i, xExtent - 2 * i, s.getHeight() - 2 * i, color);
                }
            } else {
                final int xExtent = s.getWidth();
                for (int i = 0; i < left; i++) {
                    canvas.drawRectangle(i, i, xExtent - 2 * i, s.getHeight() - 2 * i, color);
                }
                canvas.drawLine(xExtent - BORDER, left, xExtent - BORDER, left + s.getHeight(), color);
                canvas.drawSolidRectangle(xExtent - BORDER + 1, left, BORDER - 2, s.getHeight() - 2 * left, Toolkit
                        .getColor(ColorsAndFonts.COLOR_SECONDARY3));
            }
        }
    }

    @Override
    public void entered() {
        getState().setContentIdentified();
        getState().setViewIdentified();
        wrappedView.entered();
        markDamaged();
    }

    @Override
    public void exited() {
        getState().clearObjectIdentified();
        getState().clearViewIdentified();
        wrappedView.exited();
        markDamaged();
    }

    @Override
    public void secondClick(final Click click) {
    // ignore - prevents the super class opening a view
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/ServiceBorder [" + getSpecification() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
