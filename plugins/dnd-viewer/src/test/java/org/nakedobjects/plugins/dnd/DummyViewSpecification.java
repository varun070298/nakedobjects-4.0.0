package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;

public class DummyViewSpecification implements ViewSpecification {

    public View createView(final Content content, final ViewAxis axis) {
        return new DummyView();
    }

    public String getName() {
        return null;
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

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
