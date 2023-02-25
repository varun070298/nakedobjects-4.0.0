package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.ViewRequirement;


public abstract class CompositeNodeSpecification extends NodeSpecification implements CompositeViewSpecification, SubviewSpec {
    protected ViewBuilder builder;
    private NodeSpecification collectionLeafNodeSpecification;
    private NodeSpecification objectLeafNodeSpecification;

    public void setCollectionSubNodeSpecification(final NodeSpecification collectionLeafNodeSpecification) {
        this.collectionLeafNodeSpecification = collectionLeafNodeSpecification;
    }

    public void setObjectSubNodeSpecification(final NodeSpecification objectLeafNodeSpecification) {
        this.objectLeafNodeSpecification = objectLeafNodeSpecification;
    }

    @Override
    protected View createNodeView(final Content content, final ViewAxis axis) {
        return builder.createCompositeView(content, this, axis);
    }

    public View decorateSubview(final View view) {
        return view;
    }

    public ViewBuilder getSubviewBuilder() {
        return builder;
    }

    public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
        ViewRequirement requirement = new ViewRequirement(content, axis, ViewRequirement.CLOSED);
        if (collectionLeafNodeSpecification.canDisplay(content, requirement )) {
            return collectionLeafNodeSpecification.createView(content, axis);
        }

        if (objectLeafNodeSpecification.canDisplay(content, requirement)) {
            return objectLeafNodeSpecification.createView(content, axis);
        }

        return null;
    }
}
// Copyright (c) Naked Objects Group Ltd.
