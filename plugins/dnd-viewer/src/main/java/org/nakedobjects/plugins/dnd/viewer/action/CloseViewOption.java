package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class CloseViewOption extends AbstractUserAction {
    public CloseViewOption() {
        super("Close");
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        view.dispose();
    }

    @Override
    public String getDescription(final View view) {
        return "Close this " + view.getSpecification().getName();
    }

    @Override
    public String toString() {
        return new ToString(this).toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
