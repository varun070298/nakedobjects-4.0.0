package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.ObjectFieldBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;
import org.nakedobjects.runtime.context.NakedObjectsContext;


/**
 * Specification for a tree node that will display an open object as a root node or within an object.
 * 
 * @see org.nakedobjects.plugins.dnd.viewer.tree.ClosedObjectNodeSpecification for displaying a closed collection
 *      within an object.
 */
public class OpenObjectNodeSpecification extends CompositeNodeSpecification {

    public OpenObjectNodeSpecification() {
        builder = new StackLayout(new ObjectFieldBuilder(this));
    }

    /**
     * This is only used to control root nodes. Therefore a object tree can only be displayed for an object
     * with fields that are collections.
     */
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        if (content.isObject() && content.getNaked() != null) {
            final NakedObject object = content.getNaked();
            final NakedObjectAssociation[] fields = object.getSpecification().getAssociations(
                    NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), object));
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isOneToManyAssociation()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int canOpen(final Content content) {
        return CAN_OPEN;
    }

    @Override
    public boolean isOpen() {
        return true;
    }
    
    @Override
    public boolean isSubView() {
        return false;
    }

    public String getName() {
        return "Object tree node - open";
    }
}
// Copyright (c) Naked Objects Group Ltd.
