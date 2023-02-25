package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.ConsentAbstract;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class RevertFieldOption extends AbstractUserAction {
    private final TextField field;

    public RevertFieldOption(final TextField field) {
        super("Revert");
        this.field = field;
    }

    @Override
    public String getDescription(final View view) {
        return "Revert the field to it original state";
    }

    @Override
    public Consent disabled(final View view) {
        return ConsentAbstract.allowIf(field.hasInvalidEntry());
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        field.revertInvalidEntry();
    }

}

// Copyright (c) Naked Objects Group Ltd.
