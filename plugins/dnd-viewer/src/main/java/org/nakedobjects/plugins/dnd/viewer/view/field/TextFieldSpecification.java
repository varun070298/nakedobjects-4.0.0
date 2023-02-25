package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.basic.TextFieldResizeBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.lookup.OpenValueDropDownBorder;


/**
 * Creates a single line text field with the base line drawn.
 */
public class TextFieldSpecification extends AbstractFieldSpecification {
    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isTextParseable() && content instanceof TextParseableContent
                && ((TextParseableContent) content).getNoLines() == 1;
    }

    public View createView(final Content content, final ViewAxis axis) {
        final View field = new TextFieldResizeBorder(new SingleLineTextField((TextParseableContent) content, this, axis, true));
        if (content.isOptionEnabled()) {
            return new OpenValueDropDownBorder(field);
        } else {
            return field;
        }
    }

    public String getName() {
        return "Single Line Text Field";
    }

    @Override
    public boolean isAligned() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
