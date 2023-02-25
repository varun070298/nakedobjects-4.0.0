package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class CloseAllViewsForObjectOption extends AbstractUserAction {
    public CloseAllViewsForObjectOption() {
        super("Close all views for this object");
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        workspace.removeViewsFor(view.getContent().getNaked());
    }

    @Override
    public String getDescription(final View view) {
        final String title = view.getContent().title();
        return "Close all views for '" + title + "'";
    }

    @Override
    public String toString() {
        return new ToString(this).toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
