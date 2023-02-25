package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.View;


public class TableFocusManager implements FocusManager {
    private int row;
    private int cell;
    private final View table;

    public TableFocusManager(final View table) {
        this.table = table;

        focusInitialChildView();
    }

    public void focusNextView() {
        View r = table.getSubviews()[row];
        View[] cells = r.getSubviews();
        for (int j = cell + 1; j < cells.length; j++) {
            if (cells[j].canFocus()) {
                cells[cell].markDamaged();
                cell = j;
                // setFocus(cells[cell]);
                cells[j].markDamaged();
                return;
            }
        }

        row++;
        if (row == table.getSubviews().length) {
            row = 0;
        }

        r = table.getSubviews()[row];
        cells = r.getSubviews();
        for (int j = 0; j < cells.length; j++) {
            if (cells[j].canFocus()) {
                cells[cell].markDamaged();
                cell = j;
                cells[j].markDamaged();
                // setFocus(cells[cell]);
                return;
            }
        }
    }

    public void focusPreviousView() {
        View r = table.getSubviews()[row];
        View[] cells = r.getSubviews();
        for (int j = cell - 1; j >= 0; j--) {
            if (cells[j].canFocus()) {
                cells[cell].markDamaged();
                cell = j;
                cells[j].markDamaged();
                return;
            }
        }

        row--;
        if (row == -1) {
            row = table.getSubviews().length - 1;
        }

        r = table.getSubviews()[row];
        cells = r.getSubviews();
        for (int j = cells.length - 1; j >= 0; j--) {
            if (cells[j].canFocus()) {
                cells[cell].markDamaged();
                cell = j;
                cells[j].markDamaged();
                return;
            }
        }
    }

    public void focusParentView() {}

    public void focusFirstChildView() {}

    public void focusLastChildView() {}

    public void focusInitialChildView() {
        row = cell = 0;

        final View[] rows = table.getSubviews();
        if (rows.length > 0) {
            row = 0;
            final View[] cells = rows[0].getSubviews();
            for (int j = 0; j < cells.length; j++) {
                if (cells[j].canFocus()) {
                    cells[cell].markDamaged();
                    cell = j;
                    cells[j].markDamaged();
                    // setFocus(cells[cell]);
                    return;
                }
            }
        }
    }

    public View getFocus() {
        final View[] rows = table.getSubviews();
        if (row < 0 || row >= rows.length) {
            return table;
        }
        final View rowView = rows[row];
        final View[] cells = rowView.getSubviews();
        if (cell < 0 || cell >= cells.length) {
            return rowView;
        }
        return cells[cell];
    }

    public void setFocus(final View view) {
        if (view == table) {
            return;
        }

        final View[] rows = table.getSubviews();
        for (row = 0; row < rows.length; row++) {
            final View[] cells = rows[row].getSubviews();
            for (int j = 0; j < cells.length; j++) {
                if (view == cells[j] && cells[j].canFocus()) {
                    cells[cell].markDamaged();
                    cell = j;
                    cells[j].markDamaged();
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("row", row);
        str.append("cell", cell);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
