package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractViewDecorator;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


class DropDownSelection extends AbstractViewDecorator {

    protected DropDownSelection(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    public void draw(final Canvas canvas) {
        if (getState().isViewIdentified()) {
            final Color color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
            canvas.clearBackground(this, color);
        }
        canvas.offset(HPADDING, 0);
        super.draw(canvas);
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
    public void firstClick(final Click click) {
        final DropDownAxis axis = (DropDownAxis) getViewAxis();
        axis.setSelection((OptionContent) getContent());
        final View view = axis.getOriginalView();
        view.getParent().updateView();
        view.getParent().invalidateContent();
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final Size size = super.getRequiredSize(maximumSize);
        size.extendWidth(HPADDING * 2);
        return size;
    }
}
// Copyright (c) Naked Objects Group Ltd.
