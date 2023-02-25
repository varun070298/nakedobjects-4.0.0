package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;


/**
 * Specification for a tree browser frame with a tree displaying only collections and objects containing
 * collections.
 */
public class TreeSpecification implements ViewSpecification {
    private final OpenCollectionNodeSpecification openCollection;
    private final OpenObjectNodeSpecification openObject;

    public TreeSpecification() {
        final ClosedObjectNodeSpecification closedObject = new ClosedObjectNodeSpecification(false);
        final NodeSpecification closedCollection = new ClosedCollectionNodeSpecification();
        final EmptyNodeSpecification noNode = new EmptyNodeSpecification();

        openCollection = new OpenCollectionNodeSpecification();
        openCollection.setCollectionSubNodeSpecification(noNode);
        openCollection.setObjectSubNodeSpecification(closedObject);
        openCollection.setReplacementNodeSpecification(closedCollection);

        openObject = new OpenObjectNodeSpecification();
        openObject.setCollectionSubNodeSpecification(closedCollection);
        openObject.setObjectSubNodeSpecification(noNode);
        openObject.setReplacementNodeSpecification(closedObject);

        closedObject.setReplacementNodeSpecification(openObject);

        closedCollection.setReplacementNodeSpecification(openCollection);
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return requirement.is(ViewRequirement.OPEN) && (openCollection.canDisplay(content, requirement) || openObject.canDisplay(content, requirement));
    }

    public View createView(final Content content, final ViewAxis axis) {
        View rootNode;
        ViewRequirement requirement = new ViewRequirement(content, axis, ViewRequirement.CLOSED);
        if (openCollection.canDisplay(content, requirement)) {
            rootNode = openCollection.createView(content, axis);
        } else {
            rootNode = openObject.createView(content, axis);
//            frame.setSelectedNode(rootNode);
        }
        return rootNode;
    }

    public String getName() {
        return "Tree";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return false;
    }
    
    public boolean isResizeable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
