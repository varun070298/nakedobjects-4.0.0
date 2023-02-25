package org.nakedobjects.viewer.dnd.example.tree;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.util.NotImplementedException;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestObjectView;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.tree.TreeBrowserFrame;
import org.nakedobjects.viewer.dnd.tree.TreeNodeBorder;


public class NodeBorderExample extends TestViews {

    private TreeNodeBorder view;

    public static void main(final String[] args) {
        new NodeBorderExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        ViewSpecification specification = new ExampleViewSpecification();
        if (true) {
            throw new NotImplementedException("Need to create the corrext axis to for the nodes to access");
        }
        ViewAxis axis = new TreeBrowserFrame(null, null);

        Content content = new RootObject(object);
        view = new TreeNodeBorder(new TestObjectView(content, specification, axis, 200, 90, "Tree node"), null);
        view.setLocation(new Location(60, 60));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
    }

}
// Copyright (c) Naked Objects Group Ltd.
