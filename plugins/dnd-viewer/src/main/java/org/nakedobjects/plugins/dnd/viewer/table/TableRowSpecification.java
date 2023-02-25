package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;


public class TableRowSpecification extends AbstractCompositeViewSpecification {
    public TableRowSpecification() {
        builder = new TableCellBuilder();
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isObject();
    }

    @Override
    protected View decorateView(View view) {
        return new TableRowBorder(view);
    }

    public String getName() {
        return "Table Row";
    }

    @Override
    public boolean isReplaceable() {
        return false;
    }

    @Override
    public boolean isSubView() {
        return true;
    }
}
// Copyright (c) Naked Objects Group Ltd.
