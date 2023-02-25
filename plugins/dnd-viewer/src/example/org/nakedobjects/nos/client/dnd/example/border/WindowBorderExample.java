package org.nakedobjects.viewer.dnd.example.border;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.border.WindowBorder;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestObjectView;
import org.nakedobjects.viewer.dnd.example.view.TestViews;


public class WindowBorderExample extends TestViews {

    public static void main(final String[] args) {
        new WindowBorderExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        Content content = new RootObject(object);
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        View view = new WindowBorder(new TestObjectView(content, specification, axis, 300, 120, "normal"), false);
        view.setLocation(new Location(50, 60));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view = new WindowBorder(new TestObjectView(content, specification, axis, 100, 30, "active"), false);
        view.setLocation(new Location(200, 300));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view.getState().setActive();

        view = new WindowBorder(new TestObjectView(content, specification, axis, 100, 30, "view identified"), false);
        view.setLocation(new Location(200, 400));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view.getState().setInactive();
        view.getState().setRootViewIdentified();

    }

}
// Copyright (c) Naked Objects Group Ltd.
