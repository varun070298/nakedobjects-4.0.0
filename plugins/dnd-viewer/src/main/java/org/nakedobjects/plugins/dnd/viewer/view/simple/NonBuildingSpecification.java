package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;


class NonBuildingSpecification implements ViewSpecification {
    private final String name;

    public NonBuildingSpecification(final View view) {
        final String name = view.getClass().getName();
        this.name = name.substring(name.lastIndexOf('.') + 1);
    }

    public View createView(final Content content, final ViewAxis axis) {
        throw new UnexpectedCallException();
    }

    public String getName() {
        return name;
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
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
