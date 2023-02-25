package org.nakedobjects.plugins.dnd.viewer.view.form;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.DroppableLabelBorder;
import org.nakedobjects.plugins.dnd.viewer.border.IconBorder;
import org.nakedobjects.plugins.dnd.viewer.border.LabelBorder;


public class WindowFormSpecification extends AbstractFormSpecification {

    @Override
    protected View decorateView(final View formView) {
        super.decorateView(formView);
        return new IconBorder(formView);
    }

    @Override
    protected int collectionRequirement() {
        return ViewRequirement.OPEN | ViewRequirement.SUBVIEW;
    }

    @Override
    public View decorateSubview(View subview) {
        if (subview.getContent().isObject() && !subview.getContent().isTextParseable()) {
            return DroppableLabelBorder.createObjectFieldLabelBorder(subview);
        } else {
            return LabelBorder.createFieldLabelBorder(subview);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
