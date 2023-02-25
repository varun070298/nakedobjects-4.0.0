package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;


public class RootWorkspaceSpecification extends WorkspaceSpecification {
    @Override
    public View createView(final Content content, final ViewAxis axis) {
        View workspace;
        workspace = super.createView(content, axis);
        workspace.setFocusManager(new WorkspaceFocusManager());
        return workspace;
    }

    @Override
    public String getName() {
        return "Root Workspace";
    }
}
// Copyright (c) Naked Objects Group Ltd.
