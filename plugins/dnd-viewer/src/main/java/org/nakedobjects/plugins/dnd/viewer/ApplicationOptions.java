package org.nakedobjects.plugins.dnd.viewer;

import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.MenuOptions;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.help.AboutView;


public class ApplicationOptions implements MenuOptions {
    private final ShutdownListener listener;

    public ApplicationOptions(final ShutdownListener listener) {
        this.listener = listener;
    }

    public void menuOptions(final UserActionSet options) {
        options.add(new AbstractUserAction("About...") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                AboutView dialogView = new AboutView();
                final Size windowSize = dialogView.getRequiredSize(new Size());
                final Size workspaceSize = workspace.getSize();
                final int x = workspaceSize.getWidth() / 2 - windowSize.getWidth() / 2;
                final int y = workspaceSize.getHeight() / 2 - windowSize.getHeight() / 2;
                workspace.addDialog(dialogView);
                dialogView.setLocation(new Location(x, y));
            }
        });

        options.add(new AbstractUserAction("Log out") {
            @Override
            public Consent disabled(final View view) {
                final boolean runningAsExploration = view.getViewManager().isRunningAsExploration();
                if (runningAsExploration) {
                    // TODO: move logic to Facet
                    return new Veto("Can't log out in exploration mode");
                } else {
                    return Allow.DEFAULT;
                }
            }

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                listener.logOut();
            }
        });

        options.add(new AbstractUserAction("Quit") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                listener.quit();
            }
        });

    }
}

// Copyright (c) Naked Objects Group Ltd.
