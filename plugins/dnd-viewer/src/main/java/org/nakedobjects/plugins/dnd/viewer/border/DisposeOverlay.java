package org.nakedobjects.plugins.dnd.viewer.border;

import java.awt.event.KeyEvent;

import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.lookup.DropDownAxis;


public class DisposeOverlay extends AbstractViewDecorator {

    public DisposeOverlay(final View wrappedView) {
        super(wrappedView);
    }

    @Override
    public Workspace getWorkspace() {
        final View forView = ((DropDownAxis) getViewAxis()).getOriginalView();
        return forView.getWorkspace();
    }

    @Override
    public void keyPressed(final KeyboardAction key) {
        if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose();
        }
        super.keyPressed(key);
    }

    @Override
    public void dispose() {
        getViewManager().clearOverlayView(this);
    }

    @Override
    public void firstClick(final Click click) {
        super.firstClick(click);
        dispose();
    }
}
// Copyright (c) Naked Objects Group Ltd.
