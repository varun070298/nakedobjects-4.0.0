package org.nakedobjects.plugins.dnd.viewer;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.plugins.dnd.NullContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.debug.DebugFrame;
import org.nakedobjects.plugins.dnd.viewer.debug.DebugView;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class OverlayDebugFrame extends DebugFrame {
    private static final long serialVersionUID = 1L;
    private final XViewer viewer;

    public OverlayDebugFrame(final XViewer viewer) {
        super();
        this.viewer = viewer;
    }

    @Override
    protected DebugInfo[] getInfo() {
        final View overlay = viewer.getOverlayView();
        final DebugView debugView = new DebugView(overlay == null ? new EmptyView() : overlay);
        return new DebugInfo[] { debugView };
    }

    class EmptyView extends AbstractView {
        public EmptyView() {
            super(new NullContent());
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
