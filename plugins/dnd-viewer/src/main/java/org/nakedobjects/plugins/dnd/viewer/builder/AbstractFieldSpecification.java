package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;


public abstract class AbstractFieldSpecification implements ViewSpecification {
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isTextParseable() && requirement.isEditable();
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isReplaceable() {
        return true;
    }

    public boolean isSubView() {
        return true;
    }

    public boolean isAligned() {
        return false;
    }
    
    public boolean isResizeable() {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
