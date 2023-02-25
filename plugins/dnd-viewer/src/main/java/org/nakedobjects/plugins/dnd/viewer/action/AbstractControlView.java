package org.nakedobjects.plugins.dnd.viewer.action;

import java.awt.event.KeyEvent;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Feedback;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.Viewer;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public abstract class AbstractControlView implements View {
    protected final UserAction action;
    private final View parent;
    private int width;
    private int x;
    private int y;
    private int height;
    private boolean isOver;
    private boolean isPressed;

    public AbstractControlView(final UserAction action, final View target) {
        this.action = action;
        this.parent = target;
    }

    public void addView(final View view) {}

    public Consent canChangeValue() {
        return Veto.DEFAULT;
    }

    public boolean canFocus() {
        return action.disabled(parent).isAllowed();
    }

    public boolean contains(final View view) {
        return false;
    }

    public boolean containsFocus() {
        return false;
    }

    public void contentMenuOptions(final UserActionSet menuOptions) {}

    public void debug(final DebugString debug) {}

    public void debugStructure(final DebugString b) {}

    public void dispose() {}

    public void drag(final ContentDrag contentDrag) {}

    public void drag(final InternalDrag drag) {}

    public void dragCancel(final InternalDrag drag) {}

    public View dragFrom(final Location location) {
        return null;
    }

    public void dragIn(final ContentDrag drag) {}

    public void dragOut(final ContentDrag drag) {}

    public Drag dragStart(final DragStart drag) {
        return null;
    }

    public void dragTo(final InternalDrag drag) {}

    public void draw(final Canvas canvas) {}

    public void drop(final ContentDrag drag) {}

    public void drop(final ViewDrag drag) {}

    public void editComplete(boolean moveFocus, boolean toNextField) {}

    public void entered() {
        final View target = getParent();
        final Consent consent = action.disabled(target);
        String actionText = action.getName(target) + " - "
                + (consent.isVetoed() ? consent.getReason() : action.getDescription(target));
        getFeedbackManager().setAction(actionText);

        isOver = true;
        isPressed = false;
        markDamaged();
    }

    public void enteredSubview() {}

    public void exited() {
        getFeedbackManager().clearAction();
        isOver = false;
        isPressed = false;
        markDamaged();
    }

    public void exitedSubview() {}

    public void firstClick(final Click click) {
        executeAction();
    }

    private void executeAction() {
        final View target = getParent().getView();
        if (action.disabled(target).isAllowed()) {
            markDamaged();
            getViewManager().saveCurrentFieldEntry();
            action.execute(target.getWorkspace(), target, getLocation());
        }
    }

    public void focusLost() {}

    public void focusReceived() {}

    public Location getAbsoluteLocation() {
        final Location location = parent.getAbsoluteLocation();
        getViewManager().getSpy().addTrace(this, "parent location", location);
        location.add(x, y);
        getViewManager().getSpy().addTrace(this, "plus view's location", location);
        final Padding pad = parent.getPadding();
        location.add(pad.getLeft(), pad.getTop());
        getViewManager().getSpy().addTrace(this, "plus view's padding", location);
        return location;
    }

    public boolean isOver() {
        return isOver;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public int getBaseline() {
        return 0;
    }

    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    public Content getContent() {
        return null;
    }

    public int getId() {
        return 0;
    }

    public FocusManager getFocusManager() {
        return getParent() == null ? null : getParent().getFocusManager();
    }

    public Location getLocation() {
        return new Location(x, y);
    }

    public Padding getPadding() {
        return null;
    }

    public View getParent() {
        return parent;
    }

    public Size getRequiredSize(final Size maximumSize) {
        return getMaximumSize();
    }

    public Size getSize() {
        return new Size(width, height);
    }

    public ViewSpecification getSpecification() {
        return null;
    }

    public ViewState getState() {
        return null;
    }

    public View[] getSubviews() {
        return new View[0];
    }

    public View getView() {
        return this;
    }

    public ViewAxis getViewAxis() {
        return null;
    }

    public Viewer getViewManager() {
        return Toolkit.getViewer();
    }

    public Feedback getFeedbackManager() {
        return Toolkit.getFeedbackManager();
    }

    public Workspace getWorkspace() {
        return null;
    }

    public boolean hasFocus() {
        return getViewManager().hasFocus(getView());
    }

    public View identify(final Location location) {
        return this;
    }

    public void invalidateContent() {}

    public void invalidateLayout() {}

    public void keyPressed(final KeyboardAction key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            executeAction();
        }
    }

    public void keyReleased(final int keyCode, final int modifiers) {}

    public void keyTyped(final char keyCode) {}

    public void layout(final Size maximumSize) {}

    public void limitBoundsWithin(final Bounds bounds) {}

    public void limitBoundsWithin(final Size size) {}

    public void markDamaged() {
        markDamaged(getView().getBounds());
    }

    public void markDamaged(final Bounds bounds) {
        if (parent == null) {
            getViewManager().markDamaged(bounds);
        } else {
            final Location pos = parent.getLocation();
            bounds.translate(pos.getX(), pos.getY());
            parent.markDamaged(bounds);
        }
    }

    public void mouseDown(final Click click) {
        final View target = getParent().getView();
        if (action.disabled(target).isAllowed()) {
            markDamaged();
            getViewManager().saveCurrentFieldEntry();
            action.execute(target.getWorkspace(), target, getLocation());
        }
        boolean vetoed = action.disabled(target).isVetoed();
        if (!vetoed) {
            isPressed = true;
            markDamaged();
        }
    }

    public void mouseUp(final Click click) {
        final View target = getParent().getView();
        boolean vetoed = action.disabled(target).isVetoed();
        if (!vetoed) {
            isPressed = false;
            markDamaged();
        }
    }

    public void mouseMoved(final Location location) {}

    public void objectActionResult(final NakedObject result, final Location at) {}

    public View pickupContent(final Location location) {
        return null;
    }

    public View pickupView(final Location location) {
        return null;
    }

    public void print(final Canvas canvas) {}

    public void refresh() {}

    public void removeView(final View view) {}

    public void replaceView(final View toReplace, final View replacement) {}

    public void secondClick(final Click click) {}

    public void setBounds(final Bounds bounds) {}

    public void setFocusManager(final FocusManager focusManager) {}

    public void setLocation(final Location point) {
        x = point.getX();
        y = point.getY();
    }

    public void setParent(final View view) {}

    public void setMaximumSize(final Size size) {}

    public void setSize(final Size size) {
        width = size.getWidth();
        height = size.getHeight();
    }

    public void setView(final View view) {}

    public View subviewFor(final Location location) {
        return null;
    }

    public void thirdClick(final Click click) {}

    public void update(final NakedObject object) {}

    public void updateView() {}

    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return null;
    }

    public void viewMenuOptions(final UserActionSet menuOptions) {}

}
// Copyright (c) Naked Objects Group Ltd.
