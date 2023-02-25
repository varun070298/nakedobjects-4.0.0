package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.simple.ObjectView;


class LeafNodeView extends ObjectView {

    private FocusManager focusManager;

    public LeafNodeView(final Content content, final ViewSpecification design, final ViewAxis axis) {
        super(content, design, axis);
    }

    @Override
    public FocusManager getFocusManager() {
        return focusManager;
    }

    @Override
    public void setFocusManager(final FocusManager focusManager) {
        this.focusManager = focusManager;
    }
}
// Copyright (c) Naked Objects Group Ltd.
