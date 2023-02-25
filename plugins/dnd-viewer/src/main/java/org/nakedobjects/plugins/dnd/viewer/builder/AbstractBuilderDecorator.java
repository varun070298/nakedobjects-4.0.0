package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public abstract class AbstractBuilderDecorator implements ViewBuilder {
    protected final ViewBuilder wrappedBuilder;

    public AbstractBuilderDecorator(final ViewBuilder design) {
        this.wrappedBuilder = design;
        wrappedBuilder.setReference(this);
    }

    public void build(final View view) {
        wrappedBuilder.build(view);
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        return wrappedBuilder.createCompositeView(content, specification, axis);
    }

    public ViewAxis createViewAxis() {
        return wrappedBuilder.createViewAxis();
    }

    public View decorateSubview(final View subview) {
        return wrappedBuilder.decorateSubview(subview);
    }

    public ViewBuilder getReference() {
        return wrappedBuilder.getReference();
    }

    public Size getRequiredSize(final View view) {
        return wrappedBuilder.getRequiredSize(view);
    }

    public boolean isOpen() {
        return wrappedBuilder.isOpen();
    }

    public boolean isReplaceable() {
        return wrappedBuilder.isReplaceable();
    }

    public boolean isSubView() {
        return wrappedBuilder.isSubView();
    }

    public void layout(final View view, final Size maximumSize) {
        wrappedBuilder.layout(view, new Size());
    }

    public void setReference(final ViewBuilder design) {
        wrappedBuilder.setReference(design);
    }

    @Override
    public String toString() {
        final String name = getClass().getName();
        return wrappedBuilder + "/" + name.substring(name.lastIndexOf('.') + 1);
    }
}
// Copyright (c) Naked Objects Group Ltd.
