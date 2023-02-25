package org.nakedobjects.viewer.dnd.example.border;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.border.ObjectBorder;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestObjectView;
import org.nakedobjects.viewer.dnd.example.view.TestViews;


public class ObjectBorderExample extends TestViews {

    public static void main(final String[] args) {
        new ObjectBorderExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        Content content = new RootObject(object);
        View view = new ObjectBorder(1, new TestObjectView(content, specification, axis, 200, 90, "Normal"));
        view.setLocation(new Location(100, 20));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view = new ObjectBorder(4, new TestObjectView(content, specification, axis, 100, 50, "wide border"));
        view.setLocation(new Location(100, 160));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setContentIdentified();

        view = new ObjectBorder(1, new TestObjectView(content, specification, axis, 100, 50, "identified"));
        view.setLocation(new Location(100, 350));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setContentIdentified();

        view = new ObjectBorder(1, new TestObjectView(content, specification, axis, 100, 50, "active"));
        view.setLocation(new Location(100, 230));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setActive();

        view = new ObjectBorder(1, new TestObjectView(content, specification, axis, 100, 50, "can drop"));
        view.setLocation(new Location(100, 290));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setCanDrop();

        view = new ObjectBorder(1, new TestObjectView(content, specification, axis, 100, 50, "can't drop"));
        view.setLocation(new Location(100, 410));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setCantDrop();
    }

}
// Copyright (c) Naked Objects Group Ltd.
