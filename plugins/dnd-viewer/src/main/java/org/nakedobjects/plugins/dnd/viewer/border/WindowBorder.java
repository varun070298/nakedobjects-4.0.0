package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.action.CloseWindowControl;
import org.nakedobjects.plugins.dnd.viewer.action.IconizeViewOption;
import org.nakedobjects.plugins.dnd.viewer.action.IconizeWindowControl;
import org.nakedobjects.plugins.dnd.viewer.action.ResizeWindowControl;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;


public class WindowBorder extends AbstractWindowBorder {
    private static final IconizeViewOption iconizeOption = new IconizeViewOption();

    public WindowBorder(final View wrappedView, final boolean scrollable) {
        super(addTransientBorderIfNeccessary(scrollable ? new ScrollBorder(wrappedView) : wrappedView));

        if (isTransient()) {
            setControls(new WindowControl[] { new CloseWindowControl(this) });
        } else {
            setControls(new WindowControl[] { new IconizeWindowControl(this), new ResizeWindowControl(this),
                    new CloseWindowControl(this) });
        }
    }

    private static View addTransientBorderIfNeccessary(final View view) {
        final Content content = view.getContent();
        if (content.isPersistable() && content.isTransient()) {
            return new SaveTransientObjectBorder(view);
        } else {
            return view;
        }
    }

    /* TODO fix focus management and remove this hack */
    public View[] getButtons() {
        if (wrappedView instanceof ButtonBorder) {
            return ((ButtonBorder) wrappedView).getButtons();
        } else {
            return new View[0];
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (isTransient()) {
            borderRender.drawTransientMarker(canvas, getSize());
        }
    }

    private boolean isTransient() {
        final Content content = getContent();
        return content.isPersistable() && content.isTransient();
    }

    @Override
    public void viewMenuOptions(final UserActionSet menuOptions) {
        super.viewMenuOptions(menuOptions);
        menuOptions.add(iconizeOption);
    }

    @Override
    public void secondClick(final Click click) {
        if (overBorder(click.getLocation())) {
            iconizeOption.execute(getWorkspace(), getView(), getAbsoluteLocation());
        } else {
            super.secondClick(click);
        }
    }

    @Override
    protected String title() {
        return getContent().windowTitle();
    }

}
// Copyright (c) Naked Objects Group Ltd.
