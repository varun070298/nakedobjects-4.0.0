package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.ButtonAction;
import org.nakedobjects.plugins.dnd.View;


public abstract class AbstractButtonAction implements ButtonAction {
    private final String name;
    private final boolean defaultButton;

    public AbstractButtonAction(final String name) {
        this(name, false);
    }

    public AbstractButtonAction(final String name, final boolean defaultButton) {
        this.name = name;
        this.defaultButton = defaultButton;
    }

    public Consent disabled(final View view) {
        return Allow.DEFAULT;
    }

    public String getDescription(final View view) {
        return "";
    }

    public String getHelp(final View view) {
        return "No help available for button";
    }

    public String getName(final View view) {
        return name;
    }

    public NakedObjectActionType getType() {
        return NakedObjectActionConstants.USER;
    }

    public boolean isDefault() {
        return defaultButton;
    }
}
// Copyright (c) Naked Objects Group Ltd.
