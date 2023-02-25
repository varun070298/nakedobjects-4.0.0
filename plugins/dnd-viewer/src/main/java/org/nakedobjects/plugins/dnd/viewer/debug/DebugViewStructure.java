package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.View;


public class DebugViewStructure implements DebugInfo {
    private final View view;

    public DebugViewStructure(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        view.debug(debug);
    }

    public String debugTitle() {
        return "View Structure";
    }
}
// Copyright (c) Naked Objects Group Ltd.
