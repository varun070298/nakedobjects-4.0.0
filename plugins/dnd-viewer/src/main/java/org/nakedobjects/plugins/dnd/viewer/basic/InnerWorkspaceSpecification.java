package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;


public class InnerWorkspaceSpecification extends WorkspaceSpecification {
    @Override
    public View createView(final Content content, final ViewAxis axis) {
        return super.createView(content, axis);
    }

    @Override
    public String getName() {
        return "Workspace";
    }
}
// Copyright (c) Naked Objects Group Ltd.
