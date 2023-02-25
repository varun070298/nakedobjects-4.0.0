package org.nakedobjects.viewer.dnd.example.view;

import org.nakedobjects.viewer.dnd.CompositeViewBuilder;
import org.nakedobjects.viewer.dnd.CompositeViewSpecification;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.drawing.Size;


public class TestWorkspaceSpecification implements CompositeViewSpecification {

    public void setRequiredSize(final Size size) {}

    public CompositeViewBuilder getSubviewBuilder() {
        return null;
    }

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
