package org.nakedobjects.plugins.dnd;

/**
 * Describes a view, and how it is built.
 */
public interface ViewSpecification {
    /**
     * Create a new view to this specification for the specified context, and using the specified axis if
     * specified (which can be null).
     */
    View createView(Content content, ViewAxis axis);

    String getName();

    /**
     * Indicates whether views to this specification are open - displaying the attributes of the content
     * object - or are closed - display only the title of the content object.
     */
    boolean isOpen();

    /**
     * Indicates whether this view can be replaced with another view (for the same value or reference).
     * 
     * @return true if it can be replaced by another view; false if it can't be replaces
     */
    boolean isReplaceable();

    boolean isSubView();

    /**
     * Determines if the view created to this specification can display the specified type. Returns true if it
     * can.
     */
    boolean canDisplay(Content content, ViewRequirement requirement);

    /**
     * Return true if the generated views are to have their sizes adjusted so they are consistent with
     * surrounding views.
     */
    // TODO rename
    boolean isAligned();
    
    /**
     * Indicates if this view can handled being resized.  If it can't then the viewer can put it in a scroll border. 
     */
    boolean isResizeable();
}
// Copyright (c) Naked Objects Group Ltd.
