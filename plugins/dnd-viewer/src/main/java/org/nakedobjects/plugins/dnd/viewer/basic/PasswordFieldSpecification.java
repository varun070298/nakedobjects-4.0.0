package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.facets.value.PasswordValueFacet;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.field.PasswordField;


public class PasswordFieldSpecification extends AbstractFieldSpecification {
    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isTextParseable() && content.getSpecification().getFacet(PasswordValueFacet.class) != null;
    }

    public View createView(final Content content, final ViewAxis axis) {
        return new PasswordField(content, this, axis);
    }

    public String getName() {
        return "Password Field";
    }

    @Override
    public boolean isAligned() {
        return true;
    }
}
// Copyright (c) Naked Objects Group Ltd.
