package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;


public abstract class AbstractListSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {

    public AbstractListSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this));
    }
    
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isCollection() && requirement.is(ViewRequirement.OPEN);
    }

    public String getName() {
        return "List";
    }
}
// Copyright (c) Naked Objects Group Ltd.
