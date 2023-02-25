package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;


public class TableAxis implements ViewAxis {
    private final NakedObjectAssociation[] columns;
    private final String[] columnName;
    private int rowHeaderOffet;
    private View table;
    private final int[] widths;

    public TableAxis(final NakedObjectAssociation[] columns) {
        this.columns = columns;
        widths = new int[columns.length];
        columnName = new String[columns.length];
        for (int i = 0; i < widths.length; i++) {
            columnName[i] = columns[i].getName();
        }
    }

    public void ensureOffset(final int offset) {
        rowHeaderOffet = Math.max(rowHeaderOffet, offset + 5);
    }

    /**
     * Returns the number of the column found at the specificied position, ignoring the columns two borders.
     * Returns 0 for the first column, 1 for second column, etc.
     * 
     * If over the column border then returns -1.
     */
    public int getColumnAt(final int xPosition) {
        int edge = getHeaderOffset();
        for (int i = 0, cols = getColumnCount() + 1; i < cols; i++) {
            if (xPosition >= edge - 1 && xPosition <= edge + 1) {
                return -1;
            }
            if (xPosition < edge - 1) {
                return i;
            }
            edge += getColumnWidth(i);
        }

        return -1;
    }

    /**
     * Returns 0 for left side of first column, 1 for right side of first column, 2 for right side of second
     * column, etc.
     * 
     * If no column border is identified then returns -1.
     */
    public int getColumnBorderAt(final int xPosition) {
        int edge = getHeaderOffset();
        for (int i = 0, cols = getColumnCount(); i < cols; i++) {
            if (xPosition >= edge - 1 && xPosition <= edge + 1) {
                return i;
            }
            edge += getColumnWidth(i);
        }
        if (xPosition >= edge - 1 && xPosition <= edge + 1) {
            return getColumnCount();
        }

        return -1;
    }

    public int getColumnCount() {
        return columnName.length;
    }

    public String getColumnName(final int column) {
        return columnName[column];
    }

    public int getColumnWidth(final int column) {
        return widths[column];
    }

    public NakedObjectAssociation getFieldForColumn(final int column) {
        return columns[column];
    }

    public int getHeaderOffset() {
        return rowHeaderOffet;
    }

    public int getLeftEdge(final int resizeColumn) {
        int width = getHeaderOffset();
        for (int i = 0, cols = getColumnCount(); i < resizeColumn && i < cols; i++) {
            width += getColumnWidth(i);
        }
        return width;
    }

    public void invalidateLayout() {
        final View[] rows = table.getSubviews();
        for (int i = 0; i < rows.length; i++) {
            rows[i].invalidateLayout();
        }
        table.invalidateLayout();
    }

    public void setOffset(final int offset) {
        rowHeaderOffet = offset;
    }

    public void setRoot(final View view) {
        table = view;
    }

    public void setupColumnWidths(final ColumnWidthStrategy strategy) {
        for (int i = 0; i < widths.length; i++) {
            widths[i] = strategy.getPreferredWidth(i, columns[i]);
        }

    }

    public void setWidth(final int index, final int width) {
        widths[index] = width;
    }
}
// Copyright (c) Naked Objects Group Ltd.
