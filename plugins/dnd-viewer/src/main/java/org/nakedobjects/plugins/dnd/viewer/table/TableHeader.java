package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.basic.ResizeDrag;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class TableHeader extends AbstractView {
    private final int height;
    private int resizeColumn;

    public TableHeader(final Content content, final ViewAxis axis) {
        super(content, null, axis);
        height = VPADDING + Toolkit.getText(ColorsAndFonts.TEXT_LABEL).getTextHeight() + VPADDING;
    }

    @Override
    public void firstClick(final Click click) {
        if (click.getLocation().getY() <= height) {
            final TableAxis axis = ((TableAxis) getViewAxis());

            final int column = axis.getColumnAt(click.getLocation().getX()) - 1;
            if (column == -2) {
                super.firstClick(click);
            } else if (column == -1) {
                ((CollectionContent) getContent()).setOrderByElement();
                invalidateContent();
            } else {
                final NakedObjectAssociation field = axis.getFieldForColumn(column);
                ((CollectionContent) getContent()).setOrderByField(field);
                invalidateContent();
            }
        } else {
            super.firstClick(click);
        }
    }

    @Override
    public void invalidateContent() {
        getParent().invalidateContent();
    }

    @Override
    public Size getMaximumSize() {
        return new Size(-1, height);
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (isOverColumnBorder(drag.getLocation())) {
            final TableAxis axis = ((TableAxis) getViewAxis());
            resizeColumn = axis.getColumnBorderAt(drag.getLocation().getX());
            final Bounds resizeArea = new Bounds(getView().getAbsoluteLocation(), getSize());
            resizeArea.translate(getView().getPadding().getLeft(), getView().getPadding().getTop());
            if (resizeColumn == 0) {
                resizeArea.setWidth(axis.getHeaderOffset());
            } else {
                resizeArea.translate(axis.getLeftEdge(resizeColumn - 1), 0);
                resizeArea.setWidth(axis.getColumnWidth(resizeColumn - 1));
            }

            final Size minimumSize = new Size(70, 0);
            return new ResizeDrag(this, resizeArea, ResizeDrag.RIGHT, minimumSize, null);
        } else if (drag.getLocation().getY() <= height) {
            return null;
        } else {
            return super.dragStart(drag);
        }
    }

    @Override
    public void dragTo(final InternalDrag drag) {
        if (drag.getOverlay() == null) {
            throw new NakedObjectException("No overlay for drag: " + drag);
        }
        int newWidth = drag.getOverlay().getSize().getWidth();
        newWidth = Math.max(70, newWidth);
        getViewManager().getSpy().addAction("Resize column to " + newWidth);

        final TableAxis axis = ((TableAxis) getViewAxis());
        if (resizeColumn == 0) {
            axis.setOffset(newWidth);
        } else {
            axis.setWidth(resizeColumn - 1, newWidth);
        }
        axis.invalidateLayout();
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas.createSubcanvas());

        final int y = VPADDING + Toolkit.getText(ColorsAndFonts.TEXT_LABEL).getAscent();

        final TableAxis axis = ((TableAxis) getViewAxis());
        int x = axis.getHeaderOffset() - 2;

        if (((CollectionContent) getContent()).getOrderByElement()) {
            drawOrderIndicator(canvas, axis, x - 10);
        }

        Color secondary1 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        canvas.drawLine(0, height - 1, getSize().getWidth() - 1, height - 1, secondary1);
        canvas.drawLine(x, 0, x, getSize().getHeight() - 1, secondary1);
        x++;
        final int columns = axis.getColumnCount();
        final NakedObjectAssociation fieldSortOrder = ((CollectionContent) getContent()).getFieldSortOrder();
        for (int i = 0; i < columns; i++) {
            if (fieldSortOrder == axis.getFieldForColumn(i)) {
                drawOrderIndicator(canvas, axis, x + axis.getColumnWidth(i) - 10);
            }

            canvas.drawLine(x, 0, x, getSize().getHeight() - 1, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
            final Canvas headerCanvas = canvas.createSubcanvas(x, 0, axis.getColumnWidth(i) - 1, height);
            headerCanvas.drawText(axis.getColumnName(i), HPADDING, y, secondary1, Toolkit.getText(ColorsAndFonts.TEXT_LABEL));
            x += axis.getColumnWidth(i);
        }
        Color secondary2 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        canvas.drawLine(x, 0, x, getSize().getHeight() - 1, secondary2);
        canvas.drawRectangle(0, height, getSize().getWidth() - 1, getSize().getHeight() - height - 1, secondary2);
    }

    private void drawOrderIndicator(final Canvas canvas, final TableAxis axis, final int x) {
        Shape arrow;
        arrow = new Shape();
        if (((CollectionContent) getContent()).getReverseSortOrder()) {
            arrow.addVertex(0, 7);
            arrow.addVertex(3, 0);
            arrow.addVertex(6, 7);
        } else {
            arrow.addVertex(0, 0);
            arrow.addVertex(6, 0);
            arrow.addVertex(3, 7);
        }
        // canvas.drawRectangle(x + axis.getColumnWidth(i) - 10, 3, 7, 8, Toolkit.getColor("secondary3"));
        canvas.drawShape(arrow, x, 3, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
    }

    @Override
    public View identify(final Location location) {
        getViewManager().getSpy().addTrace("Identify over column " + location);
        if (isOverColumnBorder(location)) {
            getViewManager().getSpy().addAction("Identified over column ");
            return getView();
        }
        return super.identify(location);
    }

    private boolean isOverColumnBorder(final Location at) {
        final int x = at.getX();
        final TableAxis axis = ((TableAxis) getViewAxis());
        return axis.getColumnBorderAt(x) >= 0;
    }

    @Override
    public void mouseMoved(final Location at) {
        if (isOverColumnBorder(at)) {
            getFeedbackManager().showResizeRightCursor();
        } else {
            super.mouseMoved(at);
            getFeedbackManager().showDefaultCursor();
        }
    }

    @Override
    public void secondClick(final Click click) {
        if (isOverColumnBorder(click.getLocation())) {
            final TableAxis axis = ((TableAxis) getViewAxis());
            final int column = axis.getColumnBorderAt(click.getLocation().getX()) - 1;
            if (column == -1) {
                final View[] subviews = getSubviews();
                for (int i = 0; i < subviews.length; i++) {
                    final View row = subviews[i];
                    axis.ensureOffset(((TableRowBorder) row).requiredTitleWidth());
                }

            } else {
                final View[] subviews = getSubviews();
                int max = 0;
                for (int i = 0; i < subviews.length; i++) {
                    final View row = subviews[i];
                    final View cell = row.getSubviews()[column];
                    max = Math.max(max, cell.getRequiredSize(new Size()).getWidth());
                }
                axis.setWidth(column, max);
            }
            axis.invalidateLayout();
        } else {
            super.secondClick(click);
        }
    }

    @Override
    public String toString() {
        return "TableHeader";
    }

    @Override
    public ViewAreaType viewAreaType(final Location at) {
        final int x = at.getX();
        final TableAxis axis = ((TableAxis) getViewAxis());

        if (axis.getColumnBorderAt(x) >= 0) {
            return ViewAreaType.INTERNAL;
        } else {
            return super.viewAreaType(at);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
