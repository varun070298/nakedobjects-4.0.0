package org.nakedobjects.plugins.dnd.viewer.view.extendedform;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.ColumnLayout;
import org.nakedobjects.plugins.dnd.viewer.view.form.InternalFormSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class TwoPartViewSpecification extends AbstractCompositeViewSpecification {

    private class TwoPartBuilder extends AbstractViewBuilder {

        public void build(View view) {
            Content content = view.getContent();
            View form = new InternalFormSpecification().createView(content, null);
            view.addView(form);

            Content fieldContent = fieldContent(content);
            form = new InternalFormSpecification().createView(fieldContent, null);
            view.addView(form);
        }

        public View createCompositeView(Content content, CompositeViewSpecification specification, ViewAxis axis) {
            return new CompositeViewUsingBuilder(content, specification, axis);
        }
    }

    public TwoPartViewSpecification() {
        builder = new ColumnLayout(new TwoPartBuilder());
    }

    public boolean canDisplay(Content content, ViewRequirement requirement) {
        if (content.isObject() && requirement.is(ViewRequirement.OPEN)) {
            Content fieldContent = fieldContent(content);
            return fieldContent != null && fieldContent.getNaked() != null;
        } else {
            return false;
        }
    }

    private Content fieldContent(Content content) {
        NakedObjectSpecification spec = content.getSpecification();
        NakedObject target = content.getNaked();
        AuthenticationSession session = NakedObjectsContext.getAuthenticationSession();
        NakedObjectAssociation[] fields = spec.getAssociations(NakedObjectAssociationFilters.dynamicallyVisible(session, target));
        for (NakedObjectAssociation field : fields) {
            if (field.isOneToOneAssociation() && !field.getSpecification().isParseable()) {
                return Toolkit.getContentFactory().createFieldContent(field, target);
            }
        }
        return null;
    }

    public String getName() {
        return "Two part object";
    }

}

// Copyright (c) Naked Objects Group Ltd.
