package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;


public class MinimizedView extends AbstractView {
    private class CloseWindowControl extends WindowControl {

        public CloseWindowControl(final View target) {
            super(new UserAction() {
                public Consent disabled(final View view) {
                    return Allow.DEFAULT;
                }

                public void execute(final Workspace workspace, final View view, final Location at) {
                    ((MinimizedView) view).close();
                }

                public String getDescription(final View view) {
                    return "Close " + view.getSpecification().getName();
                }

                public String getHelp(final View view) {
                    return null;
                }

                public String getName(final View view) {
                    return "Close view";
                }

                public NakedObjectActionType getType() {
                    return USER;
                }
            }, target);
        }

        @Override
        public void draw(final Canvas canvas) {
            final int x = 0;
            final int y = 0;
            final Color crossColor = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
            canvas.drawLine(x + 4, y + 3, x + 10, y + 9, crossColor);
            canvas.drawLine(x + 5, y + 3, x + 11, y + 9, crossColor);
            canvas.drawLine(x + 10, y + 3, x + 4, y + 9, crossColor);
            canvas.drawLine(x + 11, y + 3, x + 5, y + 9, crossColor);
        }
    }

    private class RestoreWindowControl extends WindowControl {
        public RestoreWindowControl(final View target) {
            super(new UserAction() {

                public Consent disabled(final View view) {
                    return Allow.DEFAULT;
                }

                public void execute(final Workspace workspace, final View view, final Location at) {
                    ((MinimizedView) view).restore();
                }

                public String getDescription(final View view) {
                    return "Restore " + view.getSpecification().getName() + " to normal size";
                }

                public String getHelp(final View view) {
                    return null;
                }

                public String getName(final View view) {
                    return "Restore view";
                }

                public NakedObjectActionType getType() {
                    return USER;
                }
            }, target);
        }

        @Override
        public void draw(final Canvas canvas) {
            final int x = 0;
            final int y = 0;
            Color black = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
            canvas.drawRectangle(x + 1, y + 1, WIDTH - 1, HEIGHT - 1, black);
            canvas.drawLine(x + 2, y + 2, x + WIDTH - 2, y + 2, black);
            canvas.drawLine(x + 2, y + 3, x + WIDTH - 2, y + 3, black);
        }
    }

    private static class Specification implements ViewSpecification {

        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return false;
        }

        public View createView(final Content content, final ViewAxis axis) {
            return null;
        }

        public String getName() {
            return "minimized view";
        }

        public boolean isAligned() {
            return false;
        }

        public boolean isOpen() {
            return false;
        }

        public boolean isReplaceable() {
            return false;
        }
        
        public boolean isResizeable() {
            return false;
        }

        public boolean isSubView() {
            return false;
        }

    }

    private final static int BORDER_WIDTH = 5;
    private final WindowControl controls[];
    private View iconView;

    private final View minimizedView;

    public MinimizedView(final View viewToMinimize) {
        super(viewToMinimize.getContent(), new Specification(), null);
        this.minimizedView = viewToMinimize;
        iconView = new SubviewIconSpecification().createView(viewToMinimize.getContent(), null);
        iconView.setParent(this);
        controls = new WindowControl[] { new RestoreWindowControl(this), new CloseWindowControl(this) };
    }

    @Override
    public void debug(final DebugString debug) {
        super.debug(debug);
        debug.appendln("minimized view", minimizedView);
        debug.appendln();

        debug.appendln("icon size", iconView.getSize());
        debug.append(iconView);
    }

    @Override
    public void dispose() {
        super.dispose();
        iconView.dispose();
        // viewToMinimize.dispose();
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (iconView.getBounds().contains(drag.getLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            return iconView.dragStart(drag);
        } else {
            return super.dragStart(drag);
        }
        // View dragOverlay = new DragViewOutline(getView());
        // return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final Size size = getSize();
        final int width = size.getWidth();
        final int height = size.getHeight();
        final int left = 3;
        final int top = 3;

        final boolean hasFocus = containsFocus();
        final Color lightColor = hasFocus ? Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1) : Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        canvas.clearBackground(this, Toolkit.getColor(ColorsAndFonts.COLOR_WINDOW));
        canvas.drawRectangle(1, 0, width - 2, height, lightColor);
        canvas.drawRectangle(0, 1, width, height - 2, lightColor);
        for (int i = 2; i < left; i++) {
            canvas.drawRectangle(i, i, width - 2 * i, height - 2 * i, lightColor);
        }
        final ViewState state = getState();
        if (state.isActive()) {
            final int i = left;
            canvas.drawRectangle(i, top, width - 2 * i, height - 2 * i - top, Toolkit.getColor(ColorsAndFonts.COLOR_ACTIVE));
        }

        final int bw = controls[0].getLocation().getX() - 3; // controls.length * WindowControl.WIDTH;
        canvas.drawSolidRectangle(bw, top, width - bw - 3, height - top * 2, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));
        canvas.drawLine(bw - 1, top, bw - 1, height - top * 2, lightColor);

        for (int i = 0; controls != null && i < controls.length; i++) {
            final Canvas controlCanvas = canvas.createSubcanvas(controls[i].getBounds());
            controls[i].draw(controlCanvas);
        }

        final Canvas c = canvas.createSubcanvas(iconView.getBounds());
        iconView.draw(c);
    }

    @Override
    public Size getMaximumSize() {
        final Size size = new Size();

        size.extendWidth(BORDER_WIDTH);
        final Size iconMaximumSize = iconView.getMaximumSize();
        size.extendWidth(iconMaximumSize.getWidth());

        size.extendHeight(iconMaximumSize.getHeight());
        size.ensureHeight(WindowControl.HEIGHT);
        size.extendHeight(BORDER_WIDTH);
        size.extendHeight(BORDER_WIDTH);

        size.extendWidth(HPADDING);
        size.extendWidth(controls.length * (WindowControl.WIDTH + HPADDING));
        size.extendWidth(BORDER_WIDTH);
        return size;
    }

    @Override
    public Padding getPadding() {
        return new Padding(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
    }

    @Override
    public void layout(final Size maximumSize) {
        final Size size = getMaximumSize();

        layoutControls(size.getWidth());

        size.contractWidth(BORDER_WIDTH * 2);
        size.contractWidth(HPADDING);
        size.contractWidth(controls.length * (WindowControl.WIDTH + HPADDING));

        size.contractHeight(BORDER_WIDTH * 2);

        iconView.setLocation(new Location(BORDER_WIDTH, BORDER_WIDTH));
        iconView.setSize(size);
    }

    private void layoutControls(final int width) {
        final int widthControl = WindowControl.WIDTH + HPADDING;
        int x = width - BORDER_WIDTH + HPADDING;
        x -= widthControl * controls.length;
        final int y = BORDER_WIDTH;

        for (int i = 0; i < controls.length; i++) {
            controls[i].setSize(controls[i].getMaximumSize());
            controls[i].setLocation(new Location(x, y));
            x += widthControl;
        }
    }

    private void restore() {
        final Workspace workspace = getWorkspace();
        final View[] views = workspace.getSubviews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == this) {
                dispose();

                minimizedView.setParent(workspace);
                // workspace.removeView(this);
                workspace.addView(minimizedView);
                workspace.invalidateLayout();

                return;

            }
        }
    }

    private void close() {
        final Workspace workspace = getWorkspace();
        final View[] views = workspace.getSubviews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == this) {
                dispose();

                minimizedView.setParent(workspace);
                workspace.invalidateLayout();
                workspace.addView(minimizedView);
                minimizedView.dispose();

                return;

            }
        }
    }

    @Override
    public void removeView(final View view) {
        if (view == iconView) {
            iconView = null;
        } else {
            throw new NakedObjectException("No view " + view + " in " + this);
        }
    }

    @Override
    public void secondClick(final Click click) {
        restore();
    }

    @Override
    public ViewAreaType viewAreaType(final Location location) {
        location.subtract(BORDER_WIDTH, BORDER_WIDTH);
        return iconView.viewAreaType(location);
    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {
        options.add(new AbstractUserAction("Restore") {

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                restore();
            }
        });
        super.viewMenuOptions(options);
    }

    @Override
    public void firstClick(final Click click) {
        final View button = overControl(click.getLocation());
        if (button == null) {
            /*
             * if (overBorder(click.getLocation())) { Workspace workspace = getWorkspace(); if (workspace !=
             * null) { if (click.button2()) { workspace.lower(getView()); } else if (click.button1()) {
             * workspace.raise(getView()); } } } else { super.firstClick(click); }
             * 
             */} else {
            button.firstClick(click);
        }

    }

    private View overControl(final Location location) {
        for (int i = 0; i < controls.length; i++) {
            final WindowControl control = controls[i];
            if (control.getBounds().contains(location)) {
                return control;
            }
        }
        return null;
    }

    @Override
    public void dragIn(final ContentDrag drag) {
        if (iconView.getBounds().contains(drag.getTargetLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            iconView.dragIn(drag);
        }
    }

    @Override
    public void dragOut(final ContentDrag drag) {
        if (iconView.getBounds().contains(drag.getTargetLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            iconView.dragOut(drag);
        }
    }

    @Override
    public View identify(final Location location) {
        if (iconView.getBounds().contains(location)) {
            location.subtract(BORDER_WIDTH, BORDER_WIDTH);
            return iconView.identify(location);
        }
        return this;
    }

    @Override
    public void drop(final ContentDrag drag) {
        if (iconView.getBounds().contains(drag.getTargetLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            iconView.drop(drag);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
