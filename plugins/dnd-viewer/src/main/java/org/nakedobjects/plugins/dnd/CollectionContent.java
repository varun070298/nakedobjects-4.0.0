package org.nakedobjects.plugins.dnd;

import java.util.Enumeration;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;


public interface CollectionContent {

    Enumeration allElements();

    void debugDetails(final DebugString debug);

    NakedObject[] elements();

    NakedObject getCollection();

    void contentMenuOptions(final UserActionSet options);

    void viewMenuOptions(final UserActionSet options);

    void parseTextEntry(final String entryText);

    void setOrder(final Comparator order);

    void setOrderByField(final NakedObjectAssociation field);

    void setOrderByElement();

    NakedObjectAssociation getFieldSortOrder();

    Image getIconPicture(final int iconHeight);

    boolean getOrderByElement();

    boolean getReverseSortOrder();

    boolean isOptionEnabled();

    NakedObject[] getOptions();

    NakedObjectSpecification getElementSpecification();
}

// Copyright (c) Naked Objects Group Ltd.
