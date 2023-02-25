package org.nakedobjects.plugins.dnd.viewer.focus;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.WindowBorder;


public class SubviewFocusManager extends AbstractFocusManager {
    private final WindowBorder windowBorder;

    public SubviewFocusManager(final WindowBorder container) {
        super(container);
        windowBorder = container;
    }

    public SubviewFocusManager(final View container) {
        super(container);
        windowBorder = null;
    }

    public SubviewFocusManager(final View container, final View initalFocus) {
        super(container, initalFocus);
        windowBorder = null;
    }

    @Override
    protected View[] getChildViews() {
        final View[] subviews = container.getSubviews();
        final View[] buttons = windowBorder == null ? new View[0] : windowBorder.getButtons();

        final View[] views = new View[subviews.length + buttons.length];
        System.arraycopy(subviews, 0, views, 0, subviews.length);
        System.arraycopy(buttons, 0, views, subviews.length, buttons.length);
        return views;
    }

}
// Copyright (c) Naked Objects Group Ltd.
