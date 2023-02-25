package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class IconizeViewOption extends AbstractUserAction {
    public IconizeViewOption() {
        super("Iconize");
    }

    @Override
    public Consent disabled(final View view) {
        return Allow.DEFAULT;
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final View minimizedView = Toolkit.getViewFactory().createMinimizedView(view);
        minimizedView.setLocation(view.getLocation());
        final View[] views = workspace.getSubviews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == view) {
                workspace.removeView(view);
                workspace.addView(minimizedView);
                workspace.invalidateLayout();
                return;
            }
        }

        /*
         * // TODO change so that an iconized version of the window is created and displayed, which holds the
         * original view. View iconView = new RootIconSpecification().createView(view.getContent(), null);
         * iconView.setLocation(view.getLocation()); workspace.replaceView(view, iconView);
         */
    }

    @Override
    public String getDescription(final View view) {
        return "Show this object as an icon on the workspace";
    }
}
// Copyright (c) Naked Objects Group Ltd.
