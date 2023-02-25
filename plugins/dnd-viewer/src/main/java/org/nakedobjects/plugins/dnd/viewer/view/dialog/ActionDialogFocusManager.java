package org.nakedobjects.plugins.dnd.viewer.view.dialog;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.ButtonBorder;
import org.nakedobjects.plugins.dnd.viewer.focus.AbstractFocusManager;


public class ActionDialogFocusManager extends AbstractFocusManager {
    private final ButtonBorder buttonBorder;

    public ActionDialogFocusManager(final ButtonBorder buttonBorder) {
        super(buttonBorder.getView());
        this.buttonBorder = buttonBorder;

    }

    @Override
    protected View[] getChildViews() {
        final View[] subviews = container.getSubviews();
        final View[] buttons = buttonBorder.getButtons();

        final View[] views = new View[subviews.length + buttons.length];
        System.arraycopy(subviews, 0, views, 0, subviews.length);
        System.arraycopy(buttons, 0, views, subviews.length, buttons.length);
        return views;
    }
}
// Copyright (c) Naked Objects Group Ltd.
