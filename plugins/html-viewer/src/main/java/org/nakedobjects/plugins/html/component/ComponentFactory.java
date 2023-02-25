package org.nakedobjects.plugins.html.component;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public interface ComponentFactory {
    Component createAddOption(String id, String id2);

    Block createBlock(String style, String description);

    Component createBreadCrumbs(String[] names, boolean[] isLinked);

    Component createCollectionIcon(NakedObjectAssociation field, NakedObject collection, String id);

    DebugPane createDebugPane();

    Component createEditOption(String id);

    Component createErrorMessage(Exception e, boolean isDebug);

    Form createForm(String id, String action, int step, int noOfPages, boolean b);

    Component createHeading(String string);

    Component createInlineBlock(String style, String text, String description);

    Component createCheckboxBlock(final boolean isEditable, final boolean isSet);

    Component createSubmenu(String menuName, Component[] items);

    Component createMenuItem(
            String actionId,
            String name,
            String description,
            String reasonDisabled,
            NakedObjectActionType type,
            boolean hasParameters,
            String targetObjectId);

    Component createCollectionIcon(NakedObject object, String collectionId);

    Component createObjectIcon(NakedObject object, String objectId, String style);

    Component createObjectIcon(NakedObjectAssociation field, NakedObject object, String objectId, String style);

    Page createPage();

    Component createRemoveOption(String id, String elementId, String id2);

    Component createService(String objectId, String title, String iconName);

    Table createTable(int noColumns, boolean withSelectorColumn);

    Component createUserSwap(final String name);

    /**
     * 
     * @param field
     * @param value -
     *            may be <tt>null</tt> so subclass should handle.
     * @param isEditable
     * @return
     */
    Component createParseableField(NakedObjectAssociation field, NakedObject value, boolean isEditable);

    Component createLink(String link, String name, String description);
}

// Copyright (c) Naked Objects Group Ltd.
