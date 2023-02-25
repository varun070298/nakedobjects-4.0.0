package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;


public class NullContent implements Content {

    private final String title;

    public NullContent() {
        this("");
    }

    public NullContent(final String title) {
        this.title = title;
    }

    public Consent canDrop(final Content sourceContent) {
        return null;
    }

    public void contentMenuOptions(final UserActionSet options) {}

    public void debugDetails(final DebugString debug) {}

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public String getIconName() {
        return null;
    }

    public Image getIconPicture(final int iconHeight) {
        return null;
    }

    public String getId() {
        return null;
    }

    public NakedObject getNaked() {
        return null;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public boolean isCollection() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public boolean isPersistable() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    public boolean isTextParseable() {
        return false;
    }

    public void parseTextEntry(final String entryText) {}

    public String title() {
        return title;
    }

    public void viewMenuOptions(final UserActionSet options) {}

    public String windowTitle() {
        return title;
    }

}

// Copyright (c) Naked Objects Group Ltd.
