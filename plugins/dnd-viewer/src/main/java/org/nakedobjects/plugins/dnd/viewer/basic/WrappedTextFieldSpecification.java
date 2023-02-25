package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.field.WrappedTextField;


public class WrappedTextFieldSpecification extends AbstractFieldSpecification {
    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isTextParseable() && content instanceof TextParseableContent
                && ((TextParseableContent) content).getNoLines() > 1;
    }

    public View createView(final Content content, final ViewAxis axis) {
        final WrappedTextField wrappedTextField = new WrappedTextField((TextParseableContent) content, this, axis, true);
        wrappedTextField.setNoLines(((TextParseableContent) content).getNoLines());
        wrappedTextField.setWrapping(((TextParseableContent) content).canWrap());
        return new TextFieldResizeBorder(wrappedTextField);
    }

    public String getName() {
        return "Wrapped Text Field";
    }

    @Override
    public boolean isAligned() {
        return true;
    }

}
// Copyright (c) Naked Objects Group Ltd.
