package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;


public class DummyContent implements Content {

    private String iconName;
    private String title;
    private String windowTitle;

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public void debugDetails(final DebugString debug) {}

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public String getIconName() {
        return iconName;
    }

    public Image getIconPicture(final int iconHeight) {
        throw new NotYetImplementedException();
    }

    public NakedObject getNaked() {
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

    public boolean isPersistable() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    public boolean isTextParseable() {
        return false;
    }

    public void contentMenuOptions(final UserActionSet options) {}

    public void parseTextEntry(final String entryText) {}

    public void setupIconName(final String iconName) {
        this.iconName = iconName;
    }

    public void setupTitle(final String title) {
        this.title = title;
    }

    public void setupWindowTitle(final String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public String title() {
        return title;
    }

    public String windowTitle() {
        return windowTitle;
    }

    public String getDescription() {
        return null;
    }

    public String getId() {
        return null;
    }

    public void viewMenuOptions(final UserActionSet options) {}

    public String getHelp() {
        return null;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public boolean isOptionEnabled() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
