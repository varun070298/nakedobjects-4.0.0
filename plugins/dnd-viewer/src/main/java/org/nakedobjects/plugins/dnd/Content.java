package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;


public interface Content {

    /**
     * Determines if the specified content can be drop on this content.
     */
    Consent canDrop(Content sourceContent);

    /**
     * Allows this content to add menu options to the set of menu options the user will see for this content.
     * 
     * @see #viewMenuOptions(UserActionSet)
     */
    void contentMenuOptions(UserActionSet options);

    void debugDetails(DebugString debug);

    /**
     * Implements the response to the dropping of the specified content onto this content.
     */
    NakedObject drop(Content sourceContent);

    String getDescription();

    String getHelp();

    /**
     * The name of the icon to use to respresent the object represented by this content.
     */
    String getIconName();

    /**
     * The icon to use to respresent the object represented by this content.
     */
    Image getIconPicture(int iconHeight);

    String getId();

    /**
     * The object represented by this content.
     */
    NakedObject getNaked();

    NakedObject[] getOptions();

    /**
     * The specification of the object represented by this content.
     */
    NakedObjectSpecification getSpecification();

    /**
     * Returns true if this content represents a NakedCollection.
     */
    boolean isCollection();

    /**
     * Returns true if this content represents a NakedObject.
     */
    boolean isObject();

    /**
     * Returns true if the object represented by this content can be persisted.
     */
    boolean isPersistable();

    boolean isOptionEnabled();

    /**
     * Returns true if the object represented by this content is transient; has not been persisted yet.
     */
    boolean isTransient();

    boolean isTextParseable();

    void parseTextEntry(String entryText);

    String title();

    /**
     * Allows this content to add menu options to the set of menu options the user will see for this view.
     * 
     * @see #contentMenuOptions(UserActionSet)
     */
    void viewMenuOptions(UserActionSet options);

    String windowTitle();
}
// Copyright (c) Naked Objects Group Ltd.
