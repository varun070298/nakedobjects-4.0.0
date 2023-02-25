package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.UserActionSet;


public abstract class AbstractContent implements Content {
    public void contentMenuOptions(final UserActionSet options) {}

    public boolean isCollection() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPersistable() {
        return false;
    }

    public boolean isTextParseable() {
        return false;
    }

    public void viewMenuOptions(final UserActionSet options) {}

    public String windowTitle() {
        return "";
    }
}
// Copyright (c) Naked Objects Group Ltd.
