package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.View;


public class NullFocusManager implements FocusManager {
    private View focus;

    public void focusNextView() {}

    public void focusPreviousView() {}

    public void focusParentView() {}

    public void focusFirstChildView() {}

    public void focusLastChildView() {}

    public void focusInitialChildView() {}

    public View getFocus() {
        return focus;
    }

    public void setFocus(final View view) {
        focus = view;
    }

}
// Copyright (c) Naked Objects Group Ltd.
