package org.nakedobjects.plugins.dnd.viewer;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class KeyboardManager {
    private static final Logger LOG = Logger.getLogger(KeyboardManager.class);
    private final XViewer viewer;
    private FocusManager focusManager;

    public KeyboardManager(final XViewer viewer) {
        this.viewer = viewer;
    }

    private View getFocus() {
        return focusManager == null ? null : focusManager.getFocus();
        // View focus = viewer.getFocus();
        // return focus == null ? null : focus.getView();
    }

    /*
     * At the moment, as a fudge, the text field is calling its parent's keyPressed method for enter presses.
     */
    public void pressed(final int keyCode, final int modifiers) {
        if (ignoreKey(keyCode)) {
            return;
        }
        LOG.debug("key " + KeyEvent.getKeyModifiersText(modifiers) + " '" + KeyEvent.getKeyText(keyCode) + "' pressed");

        final KeyboardAction keyboardAction = new KeyboardAction(keyCode, modifiers);

        if (viewer.isOverlayAvailable()) {
            viewer.getOverlayView().keyPressed(keyboardAction);
            if (!keyboardAction.isConsumed() && keyCode == KeyEvent.VK_F1) {
                viewer.openHelp(viewer.getOverlayView());
                // help(viewer.getOverlayView());
            }
            return;
        }

        final View keyboardFocus = getFocus();
        if (keyboardFocus == null) {
            // throw new NakedObjectRuntimeException("No focus set");
            LOG.debug("No focus set");
            return;
        }

        keyboardFocus.keyPressed(keyboardAction);

        if (keyboardAction.isConsumed()) {
            return;
        }

        if ((modifiers & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK && keyCode == KeyEvent.VK_F10) {
            final Location location = keyboardFocus.getAbsoluteLocation();
            location.add(20, 14);
            viewer.popupMenu(keyboardFocus, location, true, false, false);
            return;
        }

        // this should really check the modifiers to ensure there are none in use.
        if (keyCode == KeyEvent.VK_F10) {
            final Location location = keyboardFocus.getAbsoluteLocation();
            location.add(20, 14);
            viewer.popupMenu(keyboardFocus, location, false, false, false);
            return;
        }
        /*
         * if(keyCode == KeyEvent.VK_ENTER) { //viewer.firstClick(new Click(keyboardFocus,
         * keyboardFocus.getLocation(), modifiers)); Location location = keyboardFocus.getAbsoluteLocation();
         * location.add(1, 1); viewer.secondClick(new Click(keyboardFocus, location, modifiers));
         * //viewer.thirdClick(new Click(keyboardFocus, keyboardFocus.getLocation(), modifiers)); return; }
         */

        if (keyCode == KeyEvent.VK_F4 && (modifiers & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
            // TODO close window
            return;
        }

        if (keyCode == KeyEvent.VK_DOWN) {
            focusManager.focusFirstChildView();
            // focusNextSubview(keyboardFocus);
            return;
        }

        if (keyCode == KeyEvent.VK_UP) {
            focusManager.focusParentView();
            // focusPreviousSubview(keyboardFocus);
            return;
        }

        if (keyCode == KeyEvent.VK_HOME) {
            viewer.makeRootFocus();
            return;
        }

        if (keyCode == KeyEvent.VK_RIGHT) {
            focusManager.focusNextView();
            // focusNextPeerView(keyboardFocus);
            return;
        }

        if (keyCode == KeyEvent.VK_LEFT) {
            focusManager.focusPreviousView();
            // focusPreviousPeerView(keyboardFocus);
            return;
        }

        int action = 0;

        if (keyCode == KeyEvent.VK_F1) {
            viewer.openHelp(keyboardFocus);
        } else if (keyCode == KeyEvent.VK_TAB) {
            action = tab(modifiers);
        }

        switch (action) {
        case KeyboardAction.NEXT_VIEW:
            focusManager.focusNextView();
            // focusNextSubview(keyboardFocus);
            break;
        case KeyboardAction.PREVIOUS_VIEW:
            focusManager.focusPreviousView();
            // focusPreviousSubview(keyboardFocus);
            break;
        case KeyboardAction.NEXT_WINDOW:
            focusManager.focusParentView();
            // focusNextRootView(keyboardFocus);
            break;
        case KeyboardAction.PREVIOUS_WINDOW:
            focusManager.focusFirstChildView();
            break;
        }
    }

    private boolean ignoreKey(final int keyCode) {
        return keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_ALT;
    }

    private int tab(final int modifiers) {
        int action;
        if ((modifiers & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
            if ((modifiers & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
                action = KeyboardAction.PREVIOUS_WINDOW;
            } else {
                action = KeyboardAction.NEXT_WINDOW;
            }
        } else {
            if ((modifiers & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
                action = KeyboardAction.PREVIOUS_VIEW;
            } else {
                action = KeyboardAction.NEXT_VIEW;
            }
        }
        return action;
    }

    public void released(final int keyCode, final int modifiers) {
        if (ignoreKey(keyCode)) {
            return;
        }

        LOG.debug("key " + KeyEvent.getKeyText(keyCode) + " released\n");
        final View keyboardFocus = getFocus();
        if (keyboardFocus != null) {
            keyboardFocus.keyReleased(keyCode, modifiers);
        }
    }

    public void typed(final char keyChar) {
        LOG.debug("typed '" + keyChar + "'");

        if (viewer.isOverlayAvailable()) {
            viewer.getOverlayView().keyTyped(keyChar);
            return;
        }

        final View keyboardFocus = getFocus();
        if (keyboardFocus != null) {
            if (!Character.isISOControl(keyChar)) {
                keyboardFocus.keyTyped(keyChar);
            }
        }
    }

    public FocusManager getFocusManager() {
        return focusManager;
    }

    public void setFocusManager(final FocusManager focusManager) {
        if (focusManager == null) {
            throw new NakedObjectException("No focus manager set up");
        }
        this.focusManager = focusManager;
    }
}
// Copyright (c) Naked Objects Group Ltd.
