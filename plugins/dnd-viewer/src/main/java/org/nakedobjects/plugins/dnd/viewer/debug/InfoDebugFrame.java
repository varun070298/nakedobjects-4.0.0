package org.nakedobjects.plugins.dnd.viewer.debug;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;


public class InfoDebugFrame extends DebugFrame {
    private static final long serialVersionUID = 1L;
    private DebugInfo[] info;

    @Override
    protected DebugInfo[] getInfo() {
        return info;
    }

    /**
     * set the display strategy to use to display the information.
     */
    public void setInfo(final DebugInfo info) {
        this.info = new DebugInfo[] { info };
    }

    /**
     * set the display strategies to use to display the information.
     */
    public void setInfo(final DebugInfo[] info) {
        this.info = info;
    }

}
// Copyright (c) Naked Objects Group Ltd.
