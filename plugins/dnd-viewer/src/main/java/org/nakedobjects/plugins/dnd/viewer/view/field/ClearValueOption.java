package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ClearValueOption extends AbstractValueOption {

    public ClearValueOption(final AbstractField field) {
        super(field, "Clear");
    }

    @Override
    public String getDescription(final View view) {
        return "Clear field";
    }

    @Override
    public Consent disabled(final View view) {
        final NakedObject value = getValue(view);
        final Consent consent = view.canChangeValue();
        if (consent.isVetoed()) {
            return consent;
        }
        if (field.cantClear()) {
            // TODO: move logic into Facets.
            return new Veto(String.format("Can't clear %s values", value.getSpecification().getShortName()));
        }
        if (value == null || isEmpty(view)) {
            // TODO: move logic into Facets.
            return new Veto("Field is already empty");
        }
        // TODO: move logic into Facets.
        return consent.setDescription(String.format("Clear value ", value.titleString()));
    }

    @Override
    public void execute(final Workspace frame, final View view, final Location at) {
        field.clear();
    }

    @Override
    public String toString() {
        return "ClearValueOption";
    }
}
// Copyright (c) Naked Objects Group Ltd.
