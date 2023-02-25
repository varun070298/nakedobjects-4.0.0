package org.nakedobjects.plugins.dnd.viewer.action;

import org.apache.log4j.Logger;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class CancelAction extends AbstractButtonAction {
    private static final Logger LOG = Logger.getLogger(CancelAction.class);

    public CancelAction() {
        super("Cancel");
    }

    public void execute(final Workspace workspace, final View view, final Location at) {
        LOG.debug("cancel pressed");
        view.dispose();
    }
}

// Copyright (c) Naked Objects Group Ltd.
