package org.nakedobjects.plugins.dnd.viewer.view.specification;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.content.ServiceObject;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;


public class ServiceIconSpecification implements ViewSpecification {

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content instanceof ServiceObject && content.getNaked().getSpecification().isService();
    }

    public View createView(final Content content, final ViewAxis axis) {
        final Icon icon = new ServiceIcon(content, this, axis);
        return new ServiceBorder(icon);
    }

    public String getName() {
        return "Service Icon";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return false;
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
}

// Copyright (c) Naked Objects Group Ltd.
