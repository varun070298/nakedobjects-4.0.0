package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public interface Workspace extends View {

    View addIconFor(NakedObject nakedObject, Location at);

    View addWindowFor(NakedObject object, Location at);

    // TODO this can probably be replace with two methods: createIcon and createView
    View createSubviewFor(NakedObject object, boolean asIcon);

    /**
     * Lower the specified view so it is below all the other views.
     */
    void lower(View view);

    /**
     * Raise the specified view so it is above all the other views.
     */
    void raise(View view);

    /**
     * Close all views for the specified object.
     */
    void removeViewsFor(NakedObject object);

    void addDialog(View dialog);

    void addWindow(View window);
}
// Copyright (c) Naked Objects Group Ltd.
