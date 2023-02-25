package org.nakedobjects.plugins.dnd.viewer.view.extendedform;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.form.InternalFormSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class ExtendedFormSpecification extends AbstractCompositeViewSpecification {

    public ExtendedFormSpecification() {
        builder = new StackLayout(new MyBuilder());
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        if (requirement.is(ViewRequirement.CLOSED)) {
            return false;
        } else {
            int collections = 0;
            if (content instanceof ObjectContent) {
                final NakedObject object = content.getNaked();
                final NakedObjectAssociation[] fields = object.getSpecification().getAssociations(
                        NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), object));
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].isOneToManyAssociation()) {
                        collections++;
                    }
                }
            }
            return collections == 1;
        }
    }

    public String getName() {
        return "Combined Form";
    }

}

class MyBuilder extends AbstractViewBuilder {

    @Override
    public void build(final View view) {
        final Content content = view.getContent();

        final View form = new InternalFormSpecification().createView(content, null);
        view.addView(form);

        /*
         * NakedObject object = ((ObjectContent) content).getObject(); NakedObjectAssociation[] fields =
         * object.getSpecification().getProperties(DynamicFilters.dynamicallyVisible(object)); for (int i = 0;
         * i < fields.length; i++) { if (fields[i].isCollection()) { } }
         */
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return false;
    }

    @Override
    public Size getRequiredSize(final View view) {
        return super.getRequiredSize(view);
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        return new CompositeViewUsingBuilder(content, specification, axis);
    }
}

// Copyright (c) Naked Objects Group Ltd.
