package org.nakedobjects.plugins.dnd.viewer.focus;

import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.View;


/**
 * Abstract focus manager that uses the set of views to move focus between from the concrete subclass.
 * 
 * @see #getChildViews()
 */
public abstract class AbstractFocusManager implements FocusManager {
    // TODO container to go in subclass ??
    protected View container;
    protected View focus;
    private final View initialFocus;

    public AbstractFocusManager(final View container) {
        this(container, null);
    }

    public AbstractFocusManager(final View container, final View initalFocus) {
        Assert.assertNotNull(container);
        this.container = container;
        this.initialFocus = initalFocus;
        focus = initalFocus;
    }

    /**
     * Throws a NakedObjectRuntimeException if the specified view is available to this focus manager.
     */
    private void checkCanFocusOn(final View view) {
        final View[] views = getChildViews();
        boolean valid = view == container.getView();
        for (int j = 0; valid == false && j < views.length; j++) {
            if (views[j] == view) {
                valid = true;
            }
        }

        if (!valid) {
            // throw new NakedObjectRuntimeException("No view " + view + " to focus on in " +
            // container.getView());
        }
    }

    public void focusFirstChildView() {
        final View[] views = getChildViews();
        for (int j = 0; j < views.length; j++) {
            if (views[j].canFocus()) {
                setFocus(views[j]);
                return;
            }
        }
        // no other focusable view; stick with the view we've got
        return;
    }

    public void focusInitialChildView() {
        if (initialFocus == null) {
            focusFirstChildView();
        } else {
            setFocus(initialFocus);
        }
    }

    public void focusLastChildView() {
        final View[] views = getChildViews();
        for (int j = views.length - 1; j > 0; j--) {
            if (views[j].canFocus()) {
                setFocus(views[j]);
                return;
            }
        }
        // no other focusable view; stick with the view we've got
        return;
    }

    public void focusNextView() {
        final View[] views = getChildViews();
        for (int i = 0; i < views.length; i++) {
            if (testView(views, i)) {
                for (int j = i + 1; j < views.length; j++) {
                    if (views[j].canFocus()) {
                        setFocus(views[j]);
                        return;
                    }
                }
                for (int j = 0; j < i; j++) {
                    if (views[j].canFocus()) {
                        setFocus(views[j]);
                        return;
                    }
                }
                // no other focusable view; stick with the view we've got
                return;
            }
        }

        // throw new NakedObjectRuntimeException();
    }

    private boolean testView(final View[] views, final int i) {
        final View view = views[i];
        return view == focus;
    }

    public void focusParentView() {
        container.getFocusManager().setFocus(container.getFocusManager().getFocus());
    }

    public void focusPreviousView() {
        final View[] views = getChildViews();
        if (views.length > 1) {
            for (int i = 0; i < views.length; i++) {
                if (testView(views, i)) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (views[j].canFocus()) {
                            setFocus(views[j]);
                            return;
                        }
                    }
                    for (int j = views.length - 1; j > i; j--) {
                        if (views[j].canFocus()) {
                            setFocus(views[j]);
                            return;
                        }
                    }
                    // no other focusable view; stick with the view we've got
                    return;
                }
            }

            // Don't move to any view
            // throw new NakedObjectRuntimeException("Can't move to previous peer from " + focus);
        }
    }

    protected abstract View[] getChildViews();

    public View getFocus() {
        return focus;
    }

    public void setFocus(final View view) {
        checkCanFocusOn(view);

        if (view != null && view.canFocus()) {
            if ((focus != null) && (focus != view)) {
                focus.focusLost();
                focus.markDamaged();
            }

            focus = view;
            focus.focusReceived();

            view.markDamaged();
        }
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("container", container);
        str.append("initialFocus", initialFocus);
        str.append("focus", focus);
        return str.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
