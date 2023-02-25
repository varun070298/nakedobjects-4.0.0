package org.nakedobjects.plugins.dnd.viewer.view.message;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.ExceptionHelper;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectApplicationException;
import org.nakedobjects.metamodel.commons.names.NameConvertorUtils;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.runtime.persistence.ConcurrencyException;


public class ExceptionMessageContent implements MessageContent {

    protected String message;
    protected String name;
    protected String trace;
    protected String title;
    private final String icon;

    public ExceptionMessageContent(final Throwable error) {
        String fullName = error.getClass().getName();
        fullName = fullName.substring(fullName.lastIndexOf('.') + 1);
        name = NameConvertorUtils.naturalName(fullName);
        message = error.getMessage();
        trace = ExceptionHelper.exceptionTraceAsString(error);
        if (trace.indexOf("\tat") != -1) {
            trace = trace.substring(trace.indexOf("\tat"));
        }

        if (name == null) {
            name = "";
        }
        if (message == null) {
            message = "";
        }
        if (trace == null) {
            trace = "";
        }

        if (error instanceof NakedObjectApplicationException) {
            title = "Application Error";
            icon = "application-error";
        } else if (error instanceof ConcurrencyException) {
            title = "Concurrency Error";
            icon = "concurrency-error";
        } else {
            title = "System Error";
            icon = "system-error";
        }

    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return trace;
    }

    public String getIconName() {
        return icon;
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
        return name;
    }

    public String getHelp() {
        return "";
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
        return name;
    }

    public void viewMenuOptions(final UserActionSet options) {}

    public String windowTitle() {
        return title;
    }

}
// Copyright (c) Naked Objects Group Ltd.
