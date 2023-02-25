package org.nakedobjects.viewer.dnd.example.border;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.border.ScrollBorder;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestObjectViewWithDragging;
import org.nakedobjects.viewer.dnd.example.view.TestViews;


public class ScrollBorderExample extends TestViews {

    public static void main(final String[] args) {
        new ScrollBorderExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        Content content = new RootObject(object);
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        View view = new ScrollBorder(new TestObjectViewWithDragging(content, specification, axis, 800, 800, "both"));
        view.setLocation(new Location(50, 60));
        view.setSize(new Size(216, 216));
        workspace.addView(view);

        view = new ScrollBorder(new TestObjectViewWithDragging(content, specification, axis, 200, 800, "vertical"));
        view.setLocation(new Location(300, 60));
        view.setSize(new Size(216, 216));
        workspace.addView(view);

        view = new ScrollBorder(new TestObjectViewWithDragging(content, specification, axis, 800, 200, "horizontal"));
        view.setLocation(new Location(550, 60));
        view.setSize(new Size(216, 216));
        workspace.addView(view);

    }

}
// Copyright (c) Naked Objects Group Ltd.
