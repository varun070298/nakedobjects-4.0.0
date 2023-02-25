package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.basic.EmptyBorder;
import org.nakedobjects.plugins.dnd.viewer.border.BackgroundBorder;
import org.nakedobjects.plugins.dnd.viewer.border.IconBorder;
import org.nakedobjects.plugins.dnd.viewer.border.LineBorder;
import org.nakedobjects.plugins.dnd.viewer.view.form.SummaryFormSpecification;


public class SummaryListWindowSpecification extends AbstractListSpecification {
   
    public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
        return new SummaryFormSpecification().createView(content, axis);
    }

    public View decorateSubview(final View subview) {
        return new EmptyBorder(3, new BackgroundBorder(new LineBorder(1, 8, new EmptyBorder(3, new IconBorder(subview)))));
    }

    public String getName() {
        return "Summary List";
    }
}
// Copyright (c) Naked Objects Group Ltd.
