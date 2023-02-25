package org.nakedobjects.plugins.dnd.viewer.view.form;

import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.LabelAxis;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewFactory;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.ObjectFieldBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;


public abstract class AbstractFormSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {

    public AbstractFormSpecification() {
        builder = new StackLayout(new ObjectFieldBuilder(this), false);
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isObject() && requirement.is(ViewRequirement.OPEN);
    }

    public String getName() {
        return "Form";
    }

    @Override
    protected ViewAxis axis(Content content) {
        return new LabelAxis();
    }

    public View createSubview(final Content content, final ViewAxis axis, int sequence) {
        final ViewFactory factory = Toolkit.getViewFactory();

        int requirement = 0;
        if (content.isObject()) {
            requirement = objectRequirement();
        } else if (content.isTextParseable()) {
            requirement = textParseableRequirement();
        } else if (content.isCollection()) {
            requirement = collectionRequirement();
        } else {
            throw new UnknownTypeException(content);
        }

        if (requirement != 0 && include(content, sequence)) {
            ViewRequirement viewRequirement = new ViewRequirement(content, axis, requirement);
            return factory.createView(viewRequirement);
        } else {
            return null;
        }
    }

    protected boolean include(Content content, int sequence) {
        return true;
    }

    protected int objectRequirement() {
        return ViewRequirement.CLOSED | ViewRequirement.SUBVIEW;
    }

    protected int textParseableRequirement() {
        return ViewRequirement.CLOSED | ViewRequirement.SUBVIEW;
    }

    protected int collectionRequirement() {
        return ViewRequirement.CLOSED | ViewRequirement.SUBVIEW;
    }
}

// Copyright (c) Naked Objects Group Ltd.
