package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;


public class PerspectiveContent extends AbstractContent {
    private final PerspectiveEntry perspective;

    public PerspectiveContent(PerspectiveEntry perspective) {
        this.perspective = perspective;
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("perspective", perspective);
    }

    public NakedObject getNaked() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getHelp() {
        return "";
    }

    public String getId() {
        return "";
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    public String title() {
        return perspective.getTitle();
    }

    @Override
    public String toString() {
        return "Perspective: " + perspective;
    }

    @Override
    public String windowTitle() {
        return perspective.getTitle();
    }

    public Consent canDrop(Content sourceContent) {
        return Veto.DEFAULT;
    }

    public NakedObject drop(Content sourceContent) {
        return null;
    }

    public String getIconName() {
        return "icon";
    }

    public Image getIconPicture(int iconHeight) {
        return null;
    }

    public void parseTextEntry(String entryText) {}

    public PerspectiveEntry getPerspective() {
        return perspective;
    }
}
// Copyright (c) Naked Objects Group Ltd.
