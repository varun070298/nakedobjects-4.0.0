package org.nakedobjects.viewer.dnd.example.tree;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.basic.TextFieldResizeBorder;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestObjectViewWithDragging;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.tree.TreeBrowserResizeBorder;


public class ResizeBorderExample extends TestViews {

    public static void main(final String[] args) {
        new ResizeBorderExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        Content content = new RootObject(object);
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        View view = new TextFieldResizeBorder(new TestObjectViewWithDragging(content, specification, axis, 400, 400, "resizing"));
        view.setLocation(new Location(50, 60));
        view.setSize(new Size(100, 24));
        workspace.addView(view);

        view = new TreeBrowserResizeBorder(new TestObjectViewWithDragging(content, specification, axis, 400, 400, "resizing"));
        view.setLocation(new Location(50, 120));
        view.setSize(new Size(200, 200));
        workspace.addView(view);
    }

}
// Copyright (c) Naked Objects Group Ltd.
