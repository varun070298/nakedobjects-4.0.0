package org.nakedobjects.viewer.dnd.example.tree;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.tree.ClosedCollectionNodeSpecification;
import org.nakedobjects.viewer.dnd.tree.TreeBrowserFrame;


public class TreeLeafNodeExample extends TestViews {

    public static void main(final String[] args) {
        new TreeLeafNodeExample();
    }

    protected void views(final Workspace workspace) {
        NakedObject object = createExampleObjectForView();
        ViewAxis axis = new TreeBrowserFrame(null, null);

        Content content = new RootObject(object);

        View view = new ClosedCollectionNodeSpecification().createView(content, axis);
        view.setLocation(new Location(100, 20));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view = new ClosedCollectionNodeSpecification().createView(content, axis);
        view.setLocation(new Location(100, 60));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setContentIdentified();

        view = new ClosedCollectionNodeSpecification().createView(content, axis);
        view.setLocation(new Location(100, 100));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
        view.getState().setCanDrop();
    }

}
// Copyright (c) Naked Objects Group Ltd.
