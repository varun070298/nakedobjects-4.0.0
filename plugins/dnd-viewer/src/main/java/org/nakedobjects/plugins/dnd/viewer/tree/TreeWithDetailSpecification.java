package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;

public class TreeWithDetailSpecification implements ViewSpecification {

    private TreeSpecification treeSpecification = new TreeSpecification();
    
    public boolean canDisplay(Content content, ViewRequirement requirement) {
        return treeSpecification.canDisplay(content, requirement);
    }

    public View createView(Content content, ViewAxis axis) {
        return new MasterDetailPanel(content, this, null, treeSpecification);
    }
    
    public String getName() {
        return "Tree and details";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return true;
    }
    
    public boolean isResizeable() {
        return true;
    }

    public boolean isSubView() {
        return false;
    }

}


// Copyright (c) Naked Objects Group Ltd.
