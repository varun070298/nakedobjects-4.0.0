package org.nakedobjects.plugins.dnd.viewer.view.help;

import org.nakedobjects.plugins.dnd.HelpViewer;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Viewer;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class InternalHelpViewer implements HelpViewer {
    private final Viewer viewer;

    public InternalHelpViewer(final Viewer viewer) {
        this.viewer = viewer;
    }

    public void open(final Location location, final String name, final String description, final String help) {
        viewer.clearAction();

        final View helpView = new HelpView(name, description, help);
        location.add(20, 20);
        helpView.setLocation(location);

        viewer.setOverlayView(helpView);
    }

}
// Copyright (c) Naked Objects Group Ltd.
