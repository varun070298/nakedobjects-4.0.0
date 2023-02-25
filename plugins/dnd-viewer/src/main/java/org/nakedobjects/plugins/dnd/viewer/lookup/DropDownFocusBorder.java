package org.nakedobjects.plugins.dnd.viewer.lookup;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;


// TODO incorporate this big hack into the rewrite of the focus mechanism
class DropDownFocusBorder extends AbstractBorder {

    protected DropDownFocusBorder(final View view) {
        super(view);
    }

    @Override
    public void keyPressed(final KeyboardAction key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            final View[] subviews = getSubviews();
            for (int i = 0; i < subviews.length; i++) {
                if (subviews[i].getState().isViewIdentified() || i == subviews.length - 1) {
                    subviews[i].exited();
                    subviews[i + 1 >= subviews.length ? 0 : i + 1].entered();
                    break;
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            final View[] subviews = getSubviews();
            for (int i = 0; i < subviews.length; i++) {
                if (subviews[i].getState().isViewIdentified() || i == subviews.length - 1) {
                    subviews[i].exited();
                    subviews[i == 0 ? subviews.length - 1 : i - 1].entered();
                    break;
                }
            }
        } else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            selectOption();
        } else if (key.getKeyCode() == KeyEvent.VK_TAB) {
            selectOption();

            final DropDownAxis axis = (DropDownAxis) getViewAxis();
            final View view = axis.getOriginalView();
            final View parentView = view.getParent();
            if (key.getModifiers() == InputEvent.SHIFT_MASK) {
                parentView.getFocusManager().focusPreviousView();
            } else {
                parentView.getFocusManager().focusNextView();
            }
        }
    }

    private void selectOption() {
        final View[] subviews = getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            if (subviews[i].getState().isViewIdentified()) {
                final DropDownAxis axis = (DropDownAxis) getViewAxis();
                axis.setSelection((OptionContent) subviews[i].getContent());
                final View view = axis.getOriginalView();
                final View parentView = view.getParent();

                // remember which view has the focus so we can reinstablish it later
                final View[] parentsSubviews = parentView.getSubviews();
                int index = 0;
                for (int j = 0; j < parentsSubviews.length; j++) {
                    if (view == parentsSubviews[j]) {
                        index = j;
                        break;
                    }
                }

                parentView.updateView();
                parentView.invalidateContent();
                parentView.getFocusManager().setFocus(parentView.getSubviews()[index]);
                getView().dispose();
                break;
            }
        }
    }

    @Override
    public void keyTyped(final char keyCode) {
        final View[] subviews = getSubviews();
        int i;
        int old = 0;
        for (i = 0; i < subviews.length; i++) {
            if (subviews[i].getState().isViewIdentified()) {
                old = i;
                i = i + 1 >= subviews.length ? 0 : i + 1;
                break;
            }
        }
        if (i == subviews.length) {
            i = 0;
        }

        final String startsWith = ("" + keyCode).toLowerCase();
        for (int j = i; j < subviews.length; j++) {
            if (subviews[j].getContent().title().toLowerCase().startsWith(startsWith)) {
                subviews[old].exited();
                subviews[j].entered();
                return;
            }
        }
        for (int j = 0; j < i; j++) {
            if (subviews[j].getContent().title().toLowerCase().startsWith(startsWith)) {
                subviews[old].exited();
                subviews[j].entered();
                return;
            }
        }

    }

}
// Copyright (c) Naked Objects Group Ltd.
