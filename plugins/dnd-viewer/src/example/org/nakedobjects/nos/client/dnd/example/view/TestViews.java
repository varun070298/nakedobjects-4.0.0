package org.nakedobjects.viewer.dnd.example.view;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.image.java.AwtTemplateImageLoaderInstaller;
import org.nakedobjects.nof.core.util.InfoDebugFrame;
import org.nakedobjects.nof.core.util.NakedObjectConfiguration;
import org.nakedobjects.nof.testsystem.TestProxyNakedObject;
import org.nakedobjects.nof.testsystem.TestProxySystem;
import org.nakedobjects.nof.testsystem.TestSpecification;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.Toolkit;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.debug.DebugView;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.image.ImageFactory;
import org.nakedobjects.viewer.dnd.notifier.ViewUpdateNotifier;
import org.nakedobjects.viewer.dnd.viewer.AwtToolkit;
import org.nakedobjects.viewer.dnd.viewer.ViewerFrame;
import org.nakedobjects.viewer.dnd.viewer.XViewer;

import org.apache.log4j.BasicConfigurator;



public class TestViews {
    protected TestProxySystem system;

    protected NakedObject createExampleObjectForView() {
        TestProxyNakedObject object = new TestProxyNakedObject();
        object.setupTitleString("ExampleObjectForView");
        object.setupSpecification(new TestSpecification());
        object.setupResolveState(ResolveState.GHOST);
        return object;
    }

    public static void main(final String[] args) {
        new TestViews();
    }

    protected TestViews() {
        BasicConfigurator.configure();

        system = new TestProxySystem();
        system.init();

        configure(NakedObjectsContext.getConfiguration());


        new ImageFactory(new AwtTemplateImageLoaderInstaller().createLoader());
        new AwtToolkit();
        

        XViewer viewer  = (XViewer) Toolkit.getViewer();
        ViewerFrame frame = new ViewerFrame();
        frame.setViewer(viewer);
        viewer.setRenderingArea(frame);
        viewer.setUpdateNotifier(new ViewUpdateNotifier());

        Toolkit.debug = false;

        Workspace workspace = workspace();
        viewer.setRootView(workspace);
        viewer.init();
        views(workspace);

        viewer.showSpy();

        InfoDebugFrame debug = new InfoDebugFrame();
        debug.setInfo(new DebugView(workspace));
        debug.setSize(800, 600);
        debug.setLocation(400, 300);
        debug.show();

        frame.setBounds(200, 100, 800, 600);
        frame.init();
        frame.show();
        viewer.sizeChange();

        debug.showDebugForPane();
    }

    protected void configure(final NakedObjectConfiguration configuration) {}

    protected void views(final Workspace workspace) {
        Content content = null;
        ViewSpecification specification = null;
        ViewAxis axis = null;
        TestObjectView view = new TestObjectView(content, specification, axis, 100, 200, "object");
        view.setLocation(new Location(100, 60));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
    }

    protected Workspace workspace() {
        TestWorkspaceView workspace = new TestWorkspaceView(null);
        workspace.setShowOutline(showOutline());
        // NOTE - viewer seems to ignore the placement of the workspace view
        // TODO fix the viewer so the root view is displayed at specified location
        // workspace.setLocation(new Location(50, 50));
        workspace.setSize(workspace.getRequiredSize(new Size()));
        return workspace;
    }

    protected boolean showOutline() {
        return false;
    }
}
// Copyright (c) Naked Objects Group Ltd.
