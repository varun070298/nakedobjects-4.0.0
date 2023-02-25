package org.nakedobjects.viewer.dnd.example;

import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;


public class ExampleViewSpecification implements ViewSpecification {

    public View createView(final Content content, final ViewAxis axis) {
        return null;
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

    public boolean isSubView() {
        return false;
    }

    public boolean canDisplay(final Content content) {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
