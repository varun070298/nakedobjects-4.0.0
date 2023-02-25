package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;


public class SimpleListSpecification extends AbstractListSpecification {

    public View createSubview(final Content content, ViewAxis axis, int fieldNumber) {
        ViewRequirement requirement = new ViewRequirement(content, axis, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW);
        return Toolkit.getViewFactory().createView(requirement);
    }
}
// Copyright (c) Naked Objects Group Ltd.
