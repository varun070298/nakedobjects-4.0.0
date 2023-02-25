package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;


/**
 * A simple specification that always returns false when asked if it can display any content.
 * 
 * @see #canDisplay(Content, ViewRequirement)
 */
public class EmptyNodeSpecification extends NodeSpecification {

    @Override
    public int canOpen(final Content content) {
        return CANT_OPEN;
    }

    @Override
    protected View createNodeView(final Content content, final ViewAxis axis) {
        return null;
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return false;
    }

    public String getName() {
        return "Empty tree node";
    }
}
// Copyright (c) Naked Objects Group Ltd.
