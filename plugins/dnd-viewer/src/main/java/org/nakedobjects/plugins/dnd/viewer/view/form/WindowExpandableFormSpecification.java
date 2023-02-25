package org.nakedobjects.plugins.dnd.viewer.view.form;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.ExpandableViewBorder;


public class WindowExpandableFormSpecification extends WindowFormSpecification {

    @Override
    public View decorateSubview(View subview) {
        if (subview.getContent().isObject() || subview.getContent().isCollection()) {
            return super.decorateSubview(new ExpandableViewBorder(subview));
        } else {
            return super.decorateSubview(subview);
        }
    }

    protected int collectionRequirement() {
        return ViewRequirement.CLOSED | ViewRequirement.SUBVIEW | ViewRequirement.EXPANDABLE;
    }

    public String getName() {
        return "Expanding Form";
    }
}
// Copyright (c) Naked Objects Group Ltd.
