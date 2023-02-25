package org.nakedobjects.plugins.dnd;

import java.util.Vector;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class DummyWorkspaceView extends DummyView implements Workspace {
    private final Vector subviews = new Vector();

    public DummyWorkspaceView() {}

    public View addIconFor(final NakedObject nakedObject, final Location at) {
        return createAndAddView();
    }

    private DummyView createAndAddView() {
        final DummyView view = new DummyView();
        addView(view);
        return view;
    }

    public View addWindowFor(final NakedObject object, final Location at) {
        return createAndAddView();
    }

    public View createSubviewFor(final NakedObject object, final boolean asIcon) {
        return createAndAddView();
    }

    @Override
    public void addView(final View view) {
        subviews.add(view);
    }

    public void lower(final View view) {}

    public void raise(final View view) {}

    @Override
    public void removeView(final View view) {
        subviews.remove(view);
    }

    @Override
    public View[] getSubviews() {
        return (View[]) subviews.toArray(new View[subviews.size()]);
    }

    public void removeViewsFor(final NakedObject object) {}

    @Override
    public Workspace getWorkspace() {
        return this;
    }

    public void removeObject(final NakedObject object) {}

    public void addDialog(View dialog) {}

    public void addWindow(View window) {}

}
// Copyright (c) Naked Objects Group Ltd.
