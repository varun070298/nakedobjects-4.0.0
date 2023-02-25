package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public abstract class WindowControl extends AbstractControlView {
    public final static int HEIGHT = 13;
    public final static int WIDTH = HEIGHT + 2;

    protected WindowControl(final UserAction action, final View target) {
        super(action, target);
    }

    public Size getMaximumSize() {
        return new Size(WIDTH, HEIGHT);
    }

}
// Copyright (c) Naked Objects Group Ltd.
