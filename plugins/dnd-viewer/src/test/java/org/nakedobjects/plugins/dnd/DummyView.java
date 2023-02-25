package org.nakedobjects.plugins.dnd;

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


public class DummyView implements View {

    private Size requiredSize;
    private Size size;
    private View parent;
    private View view;
    private Location location = new Location(0, 0);
    private Location absoluteLocation;
    private Content content;
    public int invlidateLayout;
    public int invalidateContent;

    public DummyView() {
        setView(this);
    }

    public void setMaximumSize(final Size size) {
        this.requiredSize = size;
    }

    public Size getMaximumSize() {
        return new Size(requiredSize);
    }

    public void addView(final View view) {}

    public Consent canChangeValue() {
        return Veto.DEFAULT;
    }

    public boolean canFocus() {
        return false;
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

    public void dispose() {
        getWorkspace().removeView(this);
    }

    public void drag(final InternalDrag drag) {}

    public void dragCancel(final InternalDrag drag) {}

    public View dragFrom(final Location location) {
        return null;
    }

    public void drag(final ContentDrag contentDrag) {}

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

    public void entered() {}

    public void exited() {}

    public void firstClick(final Click click) {}

    public void focusLost() {}

    public void focusReceived() {}

    public Location getAbsoluteLocation() {
        return absoluteLocation;
    }

    public int getBaseline() {
        return 0;
    }

    public Bounds getBounds() {
        return null;
    }

    public Content getContent() {
        return content;
    }

    public FocusManager getFocusManager() {
        return null;
    }

    public int getId() {
        return 0;
    }

    public Location getLocation() {
        return location;
    }

    public Padding getPadding() {
        return new Padding();
    }

    public View getParent() {
        return parent;
    }

    public Size getRequiredSize(final Size maximumSize) {
        return getMaximumSize();
    }

    public Size getSize() {
        return size;
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
        return view;
    }

    public ViewAxis getViewAxis() {
        return null;
    }

    public Viewer getViewManager() {
        return null;
    }

    public Feedback getFeedbackManager() {
        return null;
    }

    public Workspace getWorkspace() {
        return getParent() == null ? null : getParent().getWorkspace();
    }

    public boolean hasFocus() {
        return false;
    }

    public View identify(final Location mouseLocation) {
        return null;
    }

    public void invalidateContent() {
        invalidateContent++;
    }

    public void invalidateLayout() {
        invlidateLayout++;
    }

    public void keyPressed(final KeyboardAction key) {}

    public void keyReleased(final int keyCode, final int modifiers) {}

    public void keyTyped(final char keyCode) {}

    public void layout(final Size maximumSize) {}

    public void limitBoundsWithin(final Size size) {}

    public void markDamaged() {}

    public void markDamaged(final Bounds bounds) {}

    public void mouseDown(final Click click) {}

    public void mouseMoved(final Location location) {}

    public void mouseUp(final Click click) {}

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

    public void setLocation(final Location point) {}

    public void setParent(final View view) {
        parent = view.getView();
    }

    public void setSize(final Size size) {
        this.size = size;
    }

    public void setView(final View view) {
        this.view = view;
    }

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

    public void setupLocation(final Location location) {
        this.location = location;
    }

    public void setupAbsoluteLocation(final Location location) {
        this.absoluteLocation = location;
    }

    public void setupContent(final Content content) {
        this.content = content;
    }

}
// Copyright (c) Naked Objects Group Ltd.
