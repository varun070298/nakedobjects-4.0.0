package org.nakedobjects.viewer.dnd;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.Veto;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.noa.util.DebugString;
import org.nakedobjects.viewer.dnd.drawing.Image;


public class ExampleContent implements Content {

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
        return null;
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
        return "";
    }

    public String getId() {
        return "";
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
