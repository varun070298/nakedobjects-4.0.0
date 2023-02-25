package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public interface ViewBuilder {
    void build(View view);

    /**
     * Creates the composite view that this builder will create child views for, ie a table, list or form
     */
    View createCompositeView(Content content, CompositeViewSpecification specification, ViewAxis axis);

    ViewAxis createViewAxis();

    View decorateSubview(View subview);

    ViewBuilder getReference();

    Size getRequiredSize(View view);

    /**
     * Indicates whether this view is expanded, or iconized.
     * 
     * @return true if it is showing the object's details; false if it is showing the object only.
     */
    boolean isOpen();

    /**
     * Indicates whether this view can be replaced with another view (for the same value or reference).
     * 
     * @return true if it can be replaced by another view; false if it can't be replaces
     */
    boolean isReplaceable();

    boolean isSubView();

    void layout(View view, Size maximumSize);

    void setReference(ViewBuilder design);
}
// Copyright (c) Naked Objects Group Ltd.
