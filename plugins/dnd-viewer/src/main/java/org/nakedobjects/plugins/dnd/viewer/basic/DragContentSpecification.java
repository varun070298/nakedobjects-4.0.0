package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.border.LineBorder;


public class DragContentSpecification extends IconSpecification {

    @Override
    public View createView(final Content content, final ViewAxis axis) {
        final View icon = super.createView(content, axis);
        return new LineBorder(1, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1), icon);
    }
}
// Copyright (c) Naked Objects Group Ltd.
