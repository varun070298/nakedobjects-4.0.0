package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.form.WindowFormSpecification;


public class FormWithDetailSpecification implements ViewSpecification {

    public boolean canDisplay(Content content, ViewRequirement requirement) {
        return content.isObject() && requirement.is(ViewRequirement.OPEN) && containEnoughFields(content);
    }

    private boolean containEnoughFields(Content content) {
        NakedObjectSpecification specification = content.getSpecification();
        NakedObjectAssociation[] associations = specification.getAssociations(new AbstractFilter<NakedObjectAssociation>() {
            public boolean accept(NakedObjectAssociation t) {
                return t.isOneToManyAssociation() || (t.isOneToOneAssociation() && !((OneToOneAssociation)t).getSpecification().isParseable());
            }
        });
    //    int threshold = NakedObjectsContext.getConfiguration().getInteger(Properties.PROPERTY_BASE + "tree-count-threshold", 2);
        return associations.length >= 1;
    }

    public View createView(Content content, ViewAxis axis) {
        return new MasterDetailPanel(content, this, null, new WindowFormSpecification());
    }

    public String getName() {
        return "Form and details";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return true;
    }
    
    public boolean isResizeable() {
        return true;
    }

    public boolean isSubView() {
        return false;
    }

}

// Copyright (c) Naked Objects Group Ltd.
