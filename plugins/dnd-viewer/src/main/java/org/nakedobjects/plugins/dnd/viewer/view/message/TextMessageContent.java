package org.nakedobjects.plugins.dnd.viewer.view.message;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;


public class TextMessageContent implements MessageContent {
    protected final String message;
    protected final String heading;
    protected final String detail;
    protected final String title;

    public TextMessageContent(final String title, final String message) {
        final int pos = message.indexOf(':');
        if (pos > 2) {
            this.heading = message.substring(0, pos).trim();
            this.message = message.substring(pos + 1).trim();
        } else {
            this.heading = "";
            this.message = message;
        }
        this.title = title;
        this.detail = null;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public void contentMenuOptions(final UserActionSet options) {}

    public void debugDetails(final DebugString debug) {}

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public String getDescription() {
        return title;
    }

    public String getHelp() {
        return "";
    }

    public String getIconName() {
        return "message";
    }

    public Image getIconPicture(final int iconHeight) {
        return null;
    }

    public String getId() {
        return "message-exception";
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

    public boolean isPersistable() {
        return false;
    }

    public boolean isOptionEnabled() {
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
        return heading;
    }

    public void viewMenuOptions(final UserActionSet options) {}

    public String windowTitle() {
        return title;
    }

}
// Copyright (c) Naked Objects Group Ltd.
