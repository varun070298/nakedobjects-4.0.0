package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.OneToOneField;
import org.nakedobjects.plugins.dnd.ParameterContent;
import org.nakedobjects.plugins.dnd.View;


public class OpenObjectDropDownBorder extends OpenDropDownBorder {
    private static final DropDownObjectOverlaySpecification spec = new DropDownObjectOverlaySpecification();

    public OpenObjectDropDownBorder(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    protected View createOverlay() {
        final ObjectContent content = (ObjectContent) getContent();
        return spec.createView(getContent(), new ObjectDropDownAxis(content, getView()));
    }

    @Override
    protected boolean isAvailable() {
        final Content content = getContent();
        if (content instanceof OneToOneField) {
            final OneToOneField oneToOneField = ((OneToOneField) content);
            return oneToOneField.isEditable().isAllowed();
        } else if (content instanceof ParameterContent) {
            return true;
        } else {
            return false;
        }

    }
}
// Copyright (c) Naked Objects Group Ltd.
