package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ParameterContent;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.TextParseableField;
import org.nakedobjects.plugins.dnd.View;


public class OpenValueDropDownBorder extends OpenDropDownBorder {
    private static final DropDownObjectOverlaySpecification spec = new DropDownValueOverlaySpecification();

    public OpenValueDropDownBorder(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    protected View createOverlay() {
        final TextParseableContent content = (TextParseableContent) getContent();
        return spec.createView(getContent(), new ValueDropDownAxis(content, getView()));
    }

    @Override
    protected boolean isAvailable() {
        final Content content = getContent();
        if (content instanceof TextParseableField) {
            final TextParseableField oneToOneField = ((TextParseableField) content);
            return oneToOneField.isEditable().isAllowed();
        } else if (content instanceof ParameterContent) {
            return true;
        } else {
            return false;
        }

    }
}
// Copyright (c) Naked Objects Group Ltd.
