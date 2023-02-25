package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;


/**
 * Specification for a tree node that will display an open collection as a root node or within an object.
 * 
 * @see org.nakedobjects.plugins.dnd.viewer.tree.ClosedCollectionNodeSpecification for displaying a closed
 *      collection within an object.
 */
public class OpenCollectionNodeSpecification extends CompositeNodeSpecification {
    /**
     * A collection tree can only be displayed for a collection that has elements.
     */
    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        final NakedObject collection = content.getNaked();
        if (collection == null) {
            return false;
        } else {
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
            return facet != null && facet.size(collection) > 0;
        }
    }

    public OpenCollectionNodeSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this));
    }

    @Override
    public boolean isOpen() {
        return true;
    }
    
    @Override
    public boolean isSubView() {
        return false;
    }

    @Override
    public int canOpen(final Content content) {
        return CAN_OPEN;
    }

    public String getName() {
        return "Collection tree node - open";
    }
}
// Copyright (c) Naked Objects Group Ltd.
