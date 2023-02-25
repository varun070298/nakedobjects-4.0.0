package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewRequirement;


public class InternalListSpecification extends SimpleListSpecification {

    @Override
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return super.canDisplay(content, requirement) && requirement.is(ViewRequirement.SUBVIEW);
    }
    
    @Override
    protected View decorateView(View view) {
        return new InternalCollectionBorder(view);
    }
}
// Copyright (c) Naked Objects Group Ltd.
