package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.list.SimpleListSpecification;

public class ListWithDetailSpecification implements ViewSpecification {

    public boolean canDisplay(Content content, ViewRequirement requirement) {
        return content.isCollection() && requirement.is(ViewRequirement.OPEN);
    }

    public View createView(Content content, ViewAxis axis) {
        return new MasterDetailPanel(content, this, null, new SimpleListSpecification());
    }
    
    public String getName() {
        return "List and details";
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
        return false;
    }

    public boolean isSubView() {
        return false;
    }

}


// Copyright (c) Naked Objects Group Ltd.
