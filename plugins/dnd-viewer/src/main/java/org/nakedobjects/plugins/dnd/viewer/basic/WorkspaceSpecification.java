package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.content.PerspectiveContent;


public class WorkspaceSpecification implements CompositeViewSpecification {
    ApplicationWorkspaceBuilder builder = new ApplicationWorkspaceBuilder();

    public View createView(final Content content, final ViewAxis axis) {
        Workspace workspace;
        workspace = new ApplicationWorkspace(content, this, axis);
        // workspace.setFocusManager(new WorkspaceFocusManager());
        return workspace;
    }

    public ViewBuilder getSubviewBuilder() {
        return builder;
    }

    public String getName() {
        return "Root Workspace";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return false;
    }
    
    public boolean isResizeable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content instanceof PerspectiveContent;
    }
}
// Copyright (c) Naked Objects Group Ltd.
