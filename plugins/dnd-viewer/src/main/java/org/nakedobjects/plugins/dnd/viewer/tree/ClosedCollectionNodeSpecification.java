package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;


/**
 * Specification for a tree node that will display a closed collection as a root node or within an object.
 * 
 * @see org.nakedobjects.plugins.dnd.viewer.tree.OpenCollectionNodeSpecification for displaying an open collection
 *      within an object.
 */
public class ClosedCollectionNodeSpecification extends NodeSpecification {
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isCollection() && content.getNaked() != null;
    }

    @Override
    public int canOpen(final Content content) {
        final NakedObject collection = ((CollectionContent) content).getCollection();
        if (collection.getResolveState().isGhost()) {
            return UNKNOWN;
        } else {
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
            return facet.size(collection) > 0 ? CAN_OPEN : CANT_OPEN;
        }
    }

    @Override
    protected View createNodeView(final Content content, final ViewAxis axis) {
        final View treeLeafNode = new LeafNodeView(content, this, axis);
        return treeLeafNode;
    }

    public String getName() {
        return "Collection tree node - closed";
    }
}
// Copyright (c) Naked Objects Group Ltd.
