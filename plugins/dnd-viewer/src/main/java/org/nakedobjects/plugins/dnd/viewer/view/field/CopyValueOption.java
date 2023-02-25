package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class CopyValueOption extends AbstractValueOption {

    public CopyValueOption(final AbstractField field) {
        super(field, "Copy");
    }

    @Override
    public Consent disabled(final View view) {
        if (isEmpty(view)) {
            // TODO: move logic into Facets
            return new Veto("Field is empty");
        }
        // TODO: move logic into Facets
        return Allow.DEFAULT;
        //return new Allow(String.format("Copy value '%s' to clipboard", field.getSelectedText()));
    }

    @Override
    public void execute(final Workspace frame, final View view, final Location at) {
        field.copyToClipboard();
    }

    @Override
    public String toString() {
        return "CopyValueOption";
    }
}
// Copyright (c) Naked Objects Group Ltd.
