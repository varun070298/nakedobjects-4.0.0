package org.nakedobjects.viewer.dnd.example.tree;

import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.nof.testsystem.TestProxyNakedObject;
import org.nakedobjects.nof.testsystem.TestSpecification;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.basic.TreeBrowserSpecification;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.tree.TreeBrowserFrame;
import org.nakedobjects.viewer.dnd.view.form.WindowFormSpecification;



public class TreeExample extends TestViews {

    public static void main(final String[] args) {
        new TreeExample();
    }

    protected void views(final Workspace workspace) {
        TestProxyNakedObject object = new TestProxyNakedObject();
        object.setupSpecification(new TestSpecification());
        object.setupResolveState(ResolveState.TRANSIENT);
        
        ViewAxis axis = new TreeBrowserFrame(null, null);

        Content content = new RootObject(object);

        View view = new TreeBrowserSpecification().createView(content, axis);
        view.setLocation(new Location(100, 50));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view = new WindowFormSpecification().createView(content, axis);
        view.setLocation(new Location(100, 200));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

    }

}
// Copyright (c) Naked Objects Group Ltd.
