package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public abstract class AbstractViewBuilder implements ViewBuilder {
    private ViewBuilder reference;

    public abstract void build(final View view);

    public ViewAxis createViewAxis() {
        return null;
    }

    public View decorateSubview(final View subview) {
        return subview;
    }

    public ViewBuilder getReference() {
        return reference;
    }

    public Size getRequiredSize(final View view) {
        throw new NotYetImplementedException();
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isReplaceable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }

    public void layout(final View view, final Size maximumSize) {}

    public void setReference(final ViewBuilder design) {
        this.reference = design;
    }

    @Override
    public String toString() {
        final String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
// Copyright (c) Naked Objects Group Ltd.
