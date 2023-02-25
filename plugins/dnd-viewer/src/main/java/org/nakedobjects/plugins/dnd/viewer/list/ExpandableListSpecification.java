package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.ExpandableViewBorder;


public class ExpandableListSpecification extends AbstractListSpecification {

    public String getName() {
        return "Expanding List";
    }

    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isCollection() && requirement.is(ViewRequirement.CLOSED) && requirement.is(ViewRequirement.SUBVIEW) && requirement.is(ViewRequirement.SUBVIEW);
    }
    
    public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
        ViewRequirement requirement = new ViewRequirement(content, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW);
       return Toolkit.getViewFactory().createView(requirement);
    }

    @Override
    public View decorateView(View subview) {
        return new ExpandableViewBorder(subview);
    }
  
}
// Copyright (c) Naked Objects Group Ltd.
