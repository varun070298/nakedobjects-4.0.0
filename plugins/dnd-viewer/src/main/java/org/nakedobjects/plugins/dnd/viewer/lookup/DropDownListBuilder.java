package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;


public class DropDownListBuilder extends AbstractViewBuilder {
    private final SubviewSpec subviewSpecification;

    public DropDownListBuilder(final SubviewSpec subviewSpecification) {
        this.subviewSpecification = subviewSpecification;
    }

    @Override
    public void build(final View view) {
        final Content content = view.getContent();
        final NakedObject[] options = content.getOptions();

        // TODO sort list

        for (int i = 0; i < options.length; i++) {
            final Content subContent = new OptionContent(options[i]);
            final View subview = subviewSpecification.createSubview(subContent, view.getViewAxis(), i);
            view.addView(subview);
        }
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        final CompositeViewUsingBuilder view = new CompositeViewUsingBuilder(content, specification, axis);
        view.setCanDragView(false);
        return view;
    }

}

// Copyright (c) Naked Objects Group Ltd.
