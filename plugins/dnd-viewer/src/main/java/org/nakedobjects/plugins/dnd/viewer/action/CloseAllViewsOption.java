package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class CloseAllViewsOption extends AbstractUserAction {
    public CloseAllViewsOption() {
        super("Close all others");
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final View views[] = view.getWorkspace().getSubviews();

        for (int i = 0; i < views.length; i++) {
            final View otherView = views[i];
            if (otherView.getSpecification().isOpen() && otherView != view) {
                otherView.dispose();
            }
        }
    }

    @Override
    public String getDescription(final View view) {
        return "Close all views except " + view.getSpecification().getName().toLowerCase();
    }

    @Override
    public String toString() {
        return new ToString(this).toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
