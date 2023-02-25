package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.DragViewOutline;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;


// REVIEW can we use ObjectBorder to provide the basic functionality
public class TableRowBorder extends AbstractBorder {
    private static final int BORDER = 13;

    private final int baseline;
    private final IconGraphic icon;
    private final TitleText title;

    public TableRowBorder(final View wrappedRow) {
        super(wrappedRow);

        Text text = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
        icon = new IconGraphic(this, text);
        title = new ObjectTitleText(this, text);
        baseline = icon.getBaseline();

        left = requiredTitleWidth() + BORDER;

        ((TableAxis) wrappedRow.getViewAxis()).ensureOffset(left);
    }

    @Override
    public void debugDetails(final DebugString debug) {
        debug.appendln("RowBorder " + left + " pixels");
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        final int x = drag.getLocation().getX();
        final TableAxis axis = ((TableAxis) getViewAxis());
        final int left = axis.getHeaderOffset();
        ;
        if (x < left - BORDER) {
            final View dragOverlay = Toolkit.getViewFactory().getContentDragSpecification().createView(getContent(), null);
            return new ContentDrag(this, drag.getLocation(), dragOverlay);
        } else if (x < left) {
            final View dragOverlay = new DragViewOutline(getView());
            return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        final int baseline = getBaseline();

        final TableAxis axis = ((TableAxis) getViewAxis());
        final int width = axis.getHeaderOffset();
        final Size s = getSize();
        final Canvas subcanvas = canvas.createSubcanvas(0, 0, width, s.getHeight());
        int offset = HPADDING;
        icon.draw(subcanvas, offset, baseline);
        offset += icon.getSize().getWidth() + HPADDING + 0 + HPADDING;
        title.draw(subcanvas, offset, baseline, getLeft() - offset);

        final int columns = axis.getColumnCount();
        int x = -1;
        x += axis.getHeaderOffset();
        Color secondary1 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        canvas.drawLine(x - 1, 0, x - 1, s.getHeight() - 1, secondary1);
        canvas.drawLine(x, 0, x, s.getHeight() - 1, secondary1);
        for (int i = 0; i < columns; i++) {
            x += axis.getColumnWidth(i);
            canvas.drawLine(x, 0, x, s.getHeight() - 1, secondary1);
        }

        final int y = s.getHeight() - 1;
        Color secondary2 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        canvas.drawLine(0, y, s.getWidth(), y, secondary2);

        if (getState().isObjectIdentified()) {
            final int xExtent = width - 1;
            canvas.drawLine(xExtent - BORDER, top, xExtent - BORDER, top + s.getHeight() - 1, secondary2);
            canvas.drawSolidRectangle(xExtent - BORDER + 1, top, BORDER - 2, s.getHeight() - 2 * top - 1, Toolkit
                    .getColor(ColorsAndFonts.COLOR_SECONDARY3));
        }

        // components
        super.draw(canvas);
    }

    @Override
    public int getBaseline() {
        return baseline;
    }

    @Override
    protected int getLeft() {
        return ((TableAxis) wrappedView.getViewAxis()).getHeaderOffset();
    }

    protected int requiredTitleWidth() {
        return HPADDING + icon.getSize().getWidth() + HPADDING + title.getSize().getWidth() + HPADDING;
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
        final TableAxis axis = ((TableAxis) getViewAxis());
        final int left = axis.getHeaderOffset();
        ;
        final int x = click.getLocation().getX();
        if (x <= left) {
            final Location location = getAbsoluteLocation();
            location.translate(click.getLocation());
            getWorkspace().objectActionResult(getContent().getNaked(), location);
        } else {
            super.secondClick(click);
        }
    }

    @Override
    public String toString() {
        return "RowBorder/" + wrappedView;
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        if (mouseLocation.getX() <= left) {
            return ViewAreaType.CONTENT;
        } else if (mouseLocation.getX() >= getSize().getWidth() - right) {
            return ViewAreaType.VIEW;
        } else {
            return super.viewAreaType(mouseLocation);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
