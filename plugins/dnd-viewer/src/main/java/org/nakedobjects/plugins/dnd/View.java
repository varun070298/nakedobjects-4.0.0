package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public interface View extends Cloneable {
    /** Horizontal padding (||) between two components */
    public static final int HPADDING = NakedObjectsContext.getConfiguration()
            .getInteger(Properties.PROPERTY_BASE + "hpadding", 3);
    /** Vertical padding (=) between two components */
    public static final int VPADDING = NakedObjectsContext.getConfiguration()
            .getInteger(Properties.PROPERTY_BASE + "vpadding", 3);

    void addView(View view);

    /**
     * Determines if the user is able to change the held value.
     */
    Consent canChangeValue();

    /**
     * Determines whether this view accepts keyboard focus. If so focusLost and focusReceived will be called.
     */
    boolean canFocus();

    boolean contains(View view);

    boolean containsFocus();

    /**
     * Called when the popup menu is being populated for this view. Any content options that need to appear on
     * the menu should be added to the <code>menuOptions</code> object.
     */
    void contentMenuOptions(UserActionSet menuOptions);

    void debug(DebugString debug);

    void debugStructure(DebugString debug);

    /**
     * Called when a view is no longer needed and its resources can be disposed of. Dissociates this view from
     * its parent, and removes itself from the list of views that need to be updated.
     * 
     * @see #removeView(View)
     */
    void dispose();

    /**
     * Called as mouse is dragged within and without this view. This only occurs when no content or view is
     * being dragged.
     */
    void drag(InternalDrag drag);

    void dragCancel(InternalDrag drag);

    View dragFrom(Location location);

    /**
     * Called as the content being dragged is dragged into this view. This only occurs when view contents are
     * being dragged, and not when views themselves are being dragged.
     */
    void dragIn(ContentDrag drag);

    /**
     * Called as the content being dragged is dragged out of this view. This only occurs when view contents
     * are being dragged, and not when views themselves are being dragged.
     */
    void dragOut(ContentDrag drag);

    Drag dragStart(DragStart drag);

    /**
     * Called as the drag ends within and without this view. This only occurs when no content or view is being
     * dragged.
     */
    void dragTo(InternalDrag drag);

    /**
     * Called by the frame, or the parent view, when this view must redraw itself.
     */
    void draw(Canvas canvas);

    /**
     * Called as another view's contents (the source) is dropped on this view's contents (the target). The
     * source view can be obtained from the ViewDrag object.
     */
    void drop(ContentDrag drag);

    /**
     * Called as another view (the source) is dropped on this view (the target). The source view can be
     * obtained from the ViewDrag object.
     */
    void drop(ViewDrag drag);

    /**
     * Indicates that editing has been completed and the entry should be saved. Will be called by the view
     * manager when other action place within the parent.
     * 
     * @param moveFocus
     *            flags that focus should be moved from this field after the entry has been processed.
     * @param toNextField
     *            flags that the focus should be moved to the next field (if <code>true</code>) or to the
     *            previous field (if <code>false</code>). This parameter is ignored if the moveFocus parameter
     *            is <code>false</code>.
     */
    void editComplete(boolean moveFocus, boolean toNextField);

    /**
     * Called as the mouse crosses the bounds, and ends up inside, of this view. Is also called as the mouse
     * returns into this view from a contained view.
     */
    void entered();

    /**
     * Called as the mouse crosses the bounds, and ends up outside, of this view.
     */
    void exited();

    /**
     * Called when the user clicks the mouse buttone within this view.
     * 
     * @param click
     *            the location within the current view where the mouse click took place
     */
    void firstClick(Click click);

    void focusLost();

    void focusReceived();

    Location getAbsoluteLocation();

    int getBaseline();

    /**
     * Returns the bounding rectangle that describes where (within it parent), and how big, this view is.
     * 
     * @see #getSize()
     * @see #getLocation()
     * @return Bounds
     */
    Bounds getBounds();

    /**
     * get the object that this view represents
     */
    Content getContent();

    FocusManager getFocusManager();

    int getId();

    /**
     * Determines the location relative to this object's containing view
     * 
     * @see #getBounds()
     */
    Location getLocation();

    Size getMaximumSize();

    Padding getPadding();

    View getParent();

    Size getRequiredSize(Size maximumSize);

    /**
     * Determines the size of this view.
     * 
     * @see #getBounds()
     */
    Size getSize();

    ViewSpecification getSpecification();

    ViewState getState();

    View[] getSubviews();

    /**
     * returns the topmost decorator in the chain, or the view itself if not decorated.
     */
    View getView();

    ViewAxis getViewAxis();

    Viewer getViewManager();

    Feedback getFeedbackManager();

    Workspace getWorkspace();

    boolean hasFocus();

    View identify(Location mouseLocation);

    /**
     * Flags that the views do not properly represent the content, and hence it needs rebuilding. Contrast
     * this with invalidateLayout(), which deals with an a complete view, but one that is not showing
     * properly.
     * 
     * @see #invalidateLayout()
     */
    void invalidateContent();

    /**
     * Flags that the views are possibly not displaying the content fully - too small, wrong place etc -
     * although views exists for all the content. Contrast this with invalidateContent(), which deals with an
     * incomplete view.
     * 
     * @see #invalidateContent()
     */
    void invalidateLayout();

    /**
     * Called when the user presses any key on the keyboard while this view has the focus.
     * 
     * @param key
     *            TODO
     */
    void keyPressed(KeyboardAction key);

    /**
     * Called when the user releases any key on the keyboard while this view has the focus.
     */
    void keyReleased(int keyCode, int modifiers);

    /**
     * Called when the user presses a non-control key (i.e. data entry keys and not shift, up-arrow etc). Such
     * a key press will result in a prior call to <code>keyPressed</code> and a subsequent call to
     * <code>keyReleased</code>.
     */
    void keyTyped(char keyCode);

    void layout(Size maximumSize);

    /**
     * Limits the bounds of this view (normally when being moved or dropped) so it never extends beyond the
     * bounds of a view of the specified size.
     */
    void limitBoundsWithin(Size size);

    void markDamaged();

    void markDamaged(Bounds bounds);

    /**
     * Called as the mouse button is pressed down within this view. Does nothing; should be overriden when
     * needed. the position relative to the top-left of this view
     */
    void mouseDown(Click click);

    /**
     * Called as the mouse is moved around within this view. Does nothing; should be overriden when needed.
     * 
     * @param location
     *            the position relative to the top-left of this view
     */
    void mouseMoved(Location location);

    /**
     * Called as the mouse button is released within this view (assuming that it was pressed in this view).
     * Does nothing; should be overriden when needed.
     */
    void mouseUp(Click click);

    /**
     * Called when an action generates a result, allowing this view to decide what to do with it.
     * 
     * @param at
     *            the location where the action took place
     */
    void objectActionResult(NakedObject result, Location at);

    /**
     * Called as the drag of this view's content starts.
     */
    View pickupContent(Location location);

    /**
     * Called as the drag of this view starts.
     */
    View pickupView(Location location);

    void print(Canvas canvas);

    /**
     * Refreshes this view by reaccessing its content and redisplaying it.
     */
    void refresh();

    /**
     * Removes the specifed view from the subviews contained by this view.
     */
    void removeView(View view);

    void replaceView(View toReplace, View replacement);

    /**
     * Called when the user double-clicked this view. This method will have been preceded by a call to
     * <code>click</code>.
     */
    void secondClick(Click click);

    void setBounds(Bounds bounds);

    void setFocusManager(FocusManager focusManager);

    /**
     * Specifies the location of this view, relative to its enclosing view.
     */
    void setLocation(Location point);

    void setParent(View view);

    public void setMaximumSize(final Size size);

    void setSize(Size size);

    void setView(View view);

    /**
     * Identifies the subview that contains the specified location within its bounds. Returns null if no
     * subview exists for that location.
     */
    View subviewFor(Location location);

    /**
     * Called when the user triple-clicks the mouse buttone within this view. This method will have been
     * preceded by a call to <code>doubleClick</code>.
     */
    void thirdClick(Click click);

    /**
     * notification that the content of this view has changed
     */
    void update(NakedObject object);

    void updateView();

    /**
     * Determines if the user is invoking an action relating to this view, rather than to whatever this view
     * represents.
     * 
     * @param mouseLocation
     * @return true if the user is targeting the view itself, false if the user is targeting what is being
     *         represented
     */
    ViewAreaType viewAreaType(Location mouseLocation);

    /**
     * Called when the popup menu is being populated for this view. Any view options that need to appear on
     * the menu should be added to the <code>menuOptions</code> object.
     */
    void viewMenuOptions(UserActionSet menuOptions);

    void drag(ContentDrag contentDrag);
}
// Copyright (c) Naked Objects Group Ltd.
