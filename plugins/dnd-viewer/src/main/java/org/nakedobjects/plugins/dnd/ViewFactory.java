package org.nakedobjects.plugins.dnd;

import java.util.Enumeration;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;


/*
 * TODO this factory should always create views, not provide specifications; alternatively, this should be
 * called something else and always return the specification The caller would then need to call the create
 * method to create the object. The only case that would be slightly different would be the DragOutline one as
 * the Axis is never used.
 */
public interface ViewFactory extends DebugInfo {

    Enumeration closedSubviews(Content content, View view);

    View createDragViewOutline(View view);

    View createIcon(Content content);

    View createInnerWorkspace(Content content);

    View createMinimizedView(View view);

    View createWindow(Content content);

    // TODO use of this method should be replaced with a create method
    ViewSpecification getIconizedSubViewSpecification(Content content);

    // TODO use of this method should be replaced with a create method
    ViewSpecification getValueFieldSpecification(TextParseableContent content);

    ViewSpecification getContentDragSpecification();
/*
    Enumeration openRootViews(Content content, View view);

    Enumeration openSubviews(Content content, View view);
*/
    Enumeration valueViews(Content content, View view);

    ViewSpecification getOverlayViewSpecification(Content content);

    View createDialog(Content content);

    View createFieldView(ObjectContent content, ViewAxis axis);

    View createFieldView(TextParseableField content, ViewAxis axis);

    View createInternalList(OneToManyField content, ViewAxis axis);
    
    View createView(ViewRequirement requirement);

    Enumeration availableViews(ViewRequirement viewRequirement);
}

// Copyright (c) Naked Objects Group Ltd.
