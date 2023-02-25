package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.OneToOneField;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ClearOneToOneAssociationOption extends AbstractUserAction {
    public ClearOneToOneAssociationOption() {
        super("Clear association");
    }

    @Override
    public Consent disabled(final View view) {
        final OneToOneField content = ((OneToOneField) view.getContent());
        return content.canClear();
    }

    @Override
    public void execute(final Workspace frame, final View view, final Location at) {
        final OneToOneField content = ((OneToOneField) view.getContent());
        content.clear();
        view.getParent().invalidateContent();
    }
}
// Copyright (c) Naked Objects Group Ltd.
