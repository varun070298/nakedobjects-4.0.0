package org.nakedobjects.viewer.dnd.example;

import org.nakedobjects.nof.core.util.NakedObjectConfiguration;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.view.TestViews;


public class CanvasExample extends TestViews {

    public static void main(final String[] args) {
        new CanvasExample();

    }

    protected void configure(final NakedObjectConfiguration configuration) {
        configuration.add("nakedobjects.viewer.skylark.ascent-adjust", "true");
    }

    protected void views(final Workspace workspace) {
        // AbstractView.debug = true;

        View view = new TestCanvasView();
        view.setLocation(new Location(50, 60));
        view.setSize(new Size(216, 300));
        workspace.addView(view);

        view = new TestCanvasView();
        view.setLocation(new Location(300, 60));
        view.setSize(new Size(216, 300));
        workspace.addView(view);

        view = new TestCanvasView2();
        view.setLocation(new Location(570, 60));
        view.setSize(new Size(50, 70));
        workspace.addView(view);

        view = new TestCanvasView2();
        view.setLocation(new Location(570, 160));
        view.setSize(new Size(8, 5));
        workspace.addView(view);
    }

}
// Copyright (c) Naked Objects Group Ltd.
