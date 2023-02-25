package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class PasteValueOption extends AbstractValueOption {

    public PasteValueOption(final AbstractField field) {
        super(field, "Paste");
    }

    @Override
    public Consent disabled(final View view) {
        final Consent changable = view.canChangeValue();
        if (changable.isVetoed()) {
            return changable;
        } else {
            return changable.setDescription(String.format("Replace field content with '%s' from clipboard", getClipboard(view)));
        }
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        field.pasteFromClipboard();
    }

    private String getClipboard(final View view) {
        return (String) view.getViewManager().getClipboard(String.class);
    }

    @Override
    public String toString() {
        return "PasteValueOption";
    }
}
// Copyright (c) Naked Objects Group Ltd.
