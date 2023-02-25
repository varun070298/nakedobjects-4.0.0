package org.nakedobjects.plugins.dnd.viewer.view.simple;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.ConsentAbstract;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
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
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.Viewer;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.action.CloseAllViewsForObjectOption;
import org.nakedobjects.plugins.dnd.viewer.action.CloseAllViewsOption;
import org.nakedobjects.plugins.dnd.viewer.action.CloseViewOption;
import org.nakedobjects.plugins.dnd.viewer.action.OpenViewOption;
import org.nakedobjects.plugins.dnd.viewer.action.ReplaceViewOption;
import org.nakedobjects.plugins.dnd.viewer.content.RootObject;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.undo.UndoStack;


public abstract class AbstractView implements View {
    private static final UserAction CLOSE_ALL_OPTION = new CloseAllViewsOption();
    private static final UserAction CLOSE_OPTION = new CloseViewOption();
    private static final UserAction CLOSE_VIEWS_FOR_OBJECT = new CloseAllViewsForObjectOption();
    private static final Logger LOG = Logger.getLogger(AbstractView.class);

    private static int nextId = 0;
    private Content content;
    private int height;
    private int id = 0;
    private View parent;
    private ViewSpecification specification;
    private final ViewState state;
    private View view;
    private ViewAxis viewAxis;
    private int width;
    private int x;
    private int y;

    protected AbstractView(final Content content) {
        this(content, null, null);
    }

    protected AbstractView(final Content content, final ViewAxis axis) {
        this(content, null, axis);
    }

    protected AbstractView(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        if (content == null) {
            throw new IllegalArgumentException("Content not specified");
        }
        assignId();
        this.content = content;
        this.specification = specification;
        this.viewAxis = axis;
        state = new ViewState();
        view = this;
    }

    public void addView(final View view) {
        throw new NakedObjectException("Can't add views to " + this);
    }

    protected void assignId() {
        id = nextId++;
    }

    public Consent canChangeValue() {
        return Veto.DEFAULT;
    }

    public boolean canFocus() {
        return true;
    }

    public boolean contains(final View view) {
        final View[] subviews = getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            if (subviews[i] == view || (subviews[i] != null && subviews[i].contains(view))) {
                return true;
            }
        }
        return false;
    }

    public boolean containsFocus() {
        if (hasFocus()) {
            return true;
        }

        final View[] subviews = getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            if (subviews[i] != null && subviews[i].containsFocus()) {
                return true;
            }
        }
        return false;
    }

    public void contentMenuOptions(final UserActionSet options) {
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_CONTENT));

        final Content content = getContent();
        if (content != null) {
            content.contentMenuOptions(options);
        }
    }

    /**
     * Returns debug details about this view.
     */
    public void debug(final DebugString debug) {
        final String name = getClass().getName();
        debug.appendln("Root: " + name.substring(name.lastIndexOf('.') + 1) + getId());
        debug.indent();
        debug.appendln("set size", getSize());
        debug.appendln("maximum", getMaximumSize());
        debug.appendln("required", getRequiredSize(new Size()));
        debug.appendln("w/in parent", getRequiredSize(getParent() == null ? new Size() : getParent().getSize()));
        debug.appendln("parent's", getParent() == null ? new Size() : getParent().getSize());
        debug.appendln("padding", getPadding());
        debug.appendln("base line", getBaseline() + "px");
        debug.unindent();
        debug.appendln();

        debug.appendTitle("Specification");
        if (specification == null) {
            debug.append("\none");
        } else {
            debug.appendln(specification.getName());
            debug.appendln("  " + specification.getClass().getName());
            debug.appendln("  " + (specification.isOpen() ? "open" : "closed"));
            debug.appendln("  " + (specification.isReplaceable() ? "replaceable" : "non-replaceable"));
            debug.appendln("  " + (specification.isSubView() ? "subview" : "main view"));
        }

        debug.appendln();
        debug.appendTitle("View");

        debug.appendln("Changable", canChangeValue());

        debug.appendln();
        debug.appendln("Focus", (canFocus() ? "focusable" : "non-focusable"));
        debug.appendln("Has focus", hasFocus());
        debug.appendln("Contains focus", containsFocus());
        debug.appendln("Focus manager", getFocusManager());

        debug.appendln();
        debug.appendln("Self", getView());
        debug.appendln("Axis", getViewAxis());
        debug.appendln("State", getState());
        debug.appendln("Location", getLocation());

        debug.appendln("Workspace", getWorkspace());

        View p = getParent();
        debug.appendln("Parent hierarchy:" + (p == null ? "none" : ""));
        debug.indent();
        while (p != null) {
            debug.appendln(p.toString());
            p = p.getParent();
        }
        debug.unindent();


        debug.appendln();
        debug.appendln();
        debug.appendln();

        debug.appendTitle("View structure");
        // b.appendln("Built", (buildInvalid ? "no" : "yes") + ", " + buildCount + " builds");
        // b.appendln("Laid out", (layoutInvalid ? "no" : "yes") + ", " + layoutCount + " layouts");

        debug.appendln(getSpecification().getName().toUpperCase());
        debugStructure(debug);
    }

    public void debugStructure(final DebugString b) {
        b.appendln("Content", getContent() == null ? "none" : getContent());
        b.appendln("Required size ", getRequiredSize(new Size()));
        b.appendln("Bounds", getBounds());
        b.appendln("Baseline", getBaseline());
        b.appendln("Location", getAbsoluteLocation());
        final View views[] = getSubviews();
        b.indent();
        for (int i = 0; i < views.length; i++) {
            final View subview = views[i];
            b.appendln();
            b.appendln(subview.getSpecification().getName().toUpperCase());
            b.appendln("View", subview);
            subview.debugStructure(b);
        }
        b.unindent();
    }

    public void dispose() {
        View parent = getParent();
        if (parent != null) {
            parent.removeView(getView());
        }
    }

    public void drag(final InternalDrag drag) {}

    public void drag(final ContentDrag contentDrag) {}

    public void dragCancel(final InternalDrag drag) {
        getFeedbackManager().showDefaultCursor();
    }

    public View dragFrom(final Location location) {
        final View subview = subviewFor(location);
        if (subview != null) {
            location.subtract(subview.getLocation());
            return subview.dragFrom(location);
        } else {
            return null;
        }
    }

    public void dragIn(final ContentDrag drag) {}

    public void dragOut(final ContentDrag drag) {}

    public Drag dragStart(final DragStart drag) {
        final View subview = subviewFor(drag.getLocation());
        if (subview != null) {
            drag.subtract(subview.getLocation());
            return subview.dragStart(drag);
        } else {
            return null;
        }
    }

    public void dragTo(final InternalDrag drag) {}

    public void draw(final Canvas canvas) {
        if (Toolkit.debug) {
            canvas.drawDebugOutline(new Bounds(getSize()), getBaseline(), Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_VIEW));
        }
    }

    public void drop(final ContentDrag drag) {}

    /**
     * No default behaviour, views can only be dropped on workspace
     */
    public void drop(final ViewDrag drag) {}

    public void editComplete(boolean moveFocus, boolean toNextField) {}

    public void entered() {
        final Content cont = getContent();
        if (cont != null) {
            final String description = cont.getDescription();
            if (description != null && !"".equals(description)) {
                getFeedbackManager().setViewDetail(description);
            }
        }
    }

    public void exited() {}

    public void firstClick(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.firstClick(click);
        }
    }

    public void focusLost() {}

    public void focusReceived() {}

    public Location getAbsoluteLocation() {
        View parent = getParent();
        if (parent == null) {
            return getLocation();
        } else {
            final Location location = parent.getAbsoluteLocation();
            getViewManager().getSpy().addTrace(this, "parent location", location);
            location.add(x, y);
            getViewManager().getSpy().addTrace(this, "plus view's location", location);
            final Padding pad = parent.getPadding();
            location.add(pad.getLeft(), pad.getTop());
            getViewManager().getSpy().addTrace(this, "plus view's padding", location);
            return location;
        }
    }

    public int getBaseline() {
        return 0;
    }

    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    public Content getContent() {
        return content;
    }

    public FocusManager getFocusManager() {
        return getParent() == null ? null : getParent().getFocusManager();
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return new Location(x, y);
    }

    public Padding getPadding() {
        return new Padding(0, 0, 0, 0);
    }

    public final View getParent() {
        //Assert.assertEquals(parent == null ? null : parent.getView(), parent);
       // return parent;
        
        return parent == null ? null : parent.getView();
    }

    public Size getRequiredSize(final Size maximumSize) {
        return getMaximumSize();
    }

    public Size getMaximumSize() {
        return new Size();
    }

    public Size getSize() {
        return new Size(width, height);
    }

    public ViewSpecification getSpecification() {
        if (specification == null) {
            specification = new NonBuildingSpecification(this);
        }
        return specification;
    }

    public ViewState getState() {
        return state;
    }

    public View[] getSubviews() {
        return new View[0];
    }

    public final View getView() {
        return view;
    }

    public final ViewAxis getViewAxis() {
        return viewAxis;
    }

    public Viewer getViewManager() {
        return Toolkit.getViewer();
    }

    public Feedback getFeedbackManager() {
        return Toolkit.getFeedbackManager();
    }

    public Workspace getWorkspace() {
        return getParent() == null ? null : getParent().getWorkspace();
    }

    public boolean hasFocus() {
        return getViewManager().hasFocus(getView());
    }

    public View identify(final Location location) {
        final View subview = subviewFor(location);
        if (subview == null) {
            getViewManager().getSpy().addTrace(this, "mouse location within node view", location);
            getViewManager().getSpy().addTrace("----");
            return getView();
        } else {
            location.subtract(subview.getLocation());
            return subview.identify(location);
        }
    }

    public void invalidateContent() {}

    public void invalidateLayout() {
        View parent = getParent();
        if (parent != null) {
            parent.invalidateLayout();
        }
    }

    public void keyPressed(final KeyboardAction key) {}

    public void keyReleased(final int keyCode, final int modifiers) {}

    public void keyTyped(final char keyCode) {}

    public void layout(final Size maximumSize) {}

    /**
     * Limits the bounds of the this view (when being moved or dropped) so it never extends outside the
     * specified bounds e.g. outside of a parent view
     */
    public void limitBoundsWithin(final Bounds containerBounds) {
        final Bounds contentBounds = getView().getBounds();
        if (containerBounds.limitBounds(contentBounds)) {
            getView().setBounds(contentBounds);
        }
    }

    public void limitBoundsWithin(final Size size) {
        final int w = getView().getSize().getWidth();
        final int h = getView().getSize().getHeight();

        int x = getView().getLocation().getX();
        int y = getView().getLocation().getY();

        if (x + w > size.getWidth()) {
            x = size.getWidth() - w;
        }
        if (x < 0) {
            x = 0;
        }

        if (y + h > size.getHeight()) {
            y = size.getHeight() - h;
        }
        if (y < 0) {
            y = 0;
        }

        getView().setLocation(new Location(x, y));
    }

    public void markDamaged() {
        markDamaged(getView().getBounds());
    }

    public void markDamaged(final Bounds bounds) {
        View parent = getParent();
        if (parent == null) {
            getViewManager().markDamaged(bounds);
        } else {
            final Location pos = parent.getLocation();
            bounds.translate(pos.getX(), pos.getY());
            final Padding pad = parent.getPadding();
            bounds.translate(pad.getLeft(), pad.getTop());
            parent.markDamaged(bounds);
        }
    }

    public void mouseDown(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.mouseDown(click);
        }
    }

    public void mouseMoved(final Location location) {
        final View subview = subviewFor(location);
        if (subview != null) {
            location.subtract(subview.getLocation());
            subview.mouseMoved(location);
        }
    }

    public void mouseUp(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.mouseUp(click);
        }
    }

    public void objectActionResult(final NakedObject result, final Location at) {
        if (result != null) {
            final CollectionFacet facet = result.getSpecification().getFacet(CollectionFacet.class);
            NakedObject objectToDisplay = result;
            if (facet != null) {
                if (facet.size(result) == 1) {
                    objectToDisplay = facet.firstElement(result);
                }
            }
            getWorkspace().addWindowFor(objectToDisplay, at);
        }
        
    }

    public View pickupContent(final Location location) {
        final View subview = subviewFor(location);
        if (subview != null) {
            location.subtract(subview.getLocation());
            return subview.pickupView(location);
        } else {
            return Toolkit.getViewFactory().createDragViewOutline(getView());
        }
    }

    public View pickupView(final Location location) {
        final View subview = subviewFor(location);
        if (subview != null) {
            location.subtract(subview.getLocation());
            return subview.pickupView(location);
        } else {
            return null;
        }
    }

    /**
     * Delegates all printing the the draw method.
     * 
     * @see #draw(Canvas)
     */
    public void print(final Canvas canvas) {
        draw(canvas);
    }

    public void refresh() {}

    public void removeView(final View view) {
        throw new NakedObjectException();
    }

    protected void replaceOptions(final Enumeration possibleViews, final UserActionSet options) {
        while (possibleViews.hasMoreElements()) {
            final ViewSpecification specification = (ViewSpecification) possibleViews.nextElement();

            if (specification != getSpecification() && view.getParent() == view.getWorkspace() && view.getClass() != getClass()) {
                final UserAction viewAs = new ReplaceViewOption(specification);
                options.add(viewAs);
            }
        }
    }

    public void replaceView(final View toReplace, final View replacement) {
        throw new NakedObjectException();
    }

    public void secondClick(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.secondClick(click);
        }
    }

    public void setBounds(final Bounds bounds) {
        x = bounds.getX();
        y = bounds.getY();
        width = bounds.getWidth();
        height = bounds.getHeight();
    }

    public void setFocusManager(final FocusManager focusManager) {
        throw new UnexpectedCallException();
    }

    protected void setContent(final Content content) {
        this.content = content;
    }

    public void setLocation(final Location location) {
        x = location.getX();
        y = location.getY();
    }

    public final void setParent(final View parentView) {
        LOG.debug("set parent " + parentView + " for " + this);
        parent = parentView.getView();
    }

    public void setMaximumSize(final Size size) {}

    public void setSize(final Size size) {
        width = size.getWidth();
        height = size.getHeight();
    }

    protected void setSpecification(final ViewSpecification specification) {
        this.specification = specification;
    }

    public final void setView(final View view) {
        this.view = view;
    }

    protected void setViewAxis(final ViewAxis viewAxis) {
        this.viewAxis = viewAxis;
    }

    public View subviewFor(final Location location) {
        return null;
    }

    public void thirdClick(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.thirdClick(click);
        }
    }

    @Override
    public String toString() {
        final String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1) + getId() + ":" + getState() + ":" + getContent();
    }

    public void update(final NakedObject object) {}

    public void updateView() {}

    public ViewAreaType viewAreaType(final Location location) {
        final View subview = subviewFor(location);
        if (subview != null) {
            location.subtract(subview.getLocation());
            return subview.viewAreaType(location);
        } else {
            return ViewAreaType.CONTENT;
        }
    }

    public void viewMenuOptions(final UserActionSet options) {
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_VIEW));

        final Content content = getContent();
        if (content != null) {
            content.viewMenuOptions(options);
        }

        if (getParent() != null) {
            final Enumeration possibleViews = Toolkit.getViewFactory().availableViews(new ViewRequirement(content, ViewRequirement.OPEN)); //openRootViews(content, null);
            while (possibleViews.hasMoreElements()) {
                final ViewSpecification specification = (ViewSpecification) possibleViews.nextElement();
                final AbstractUserAction viewAs = new OpenViewOption(specification);
                options.add(viewAs);
            }
        }

        if (view.getSpecification() != null && (view.getSpecification().isSubView() && !(view.getContent() instanceof RootObject))) {
            if (view.getSpecification().isReplaceable()) {
                replaceOptions(Toolkit.getViewFactory().availableViews(new ViewRequirement(content, ViewRequirement.OPEN | ViewRequirement.REPLACEABLE)), options);  // openSubviews(content, this), options);
                replaceOptions(Toolkit.getViewFactory().availableViews(new ViewRequirement(content, ViewRequirement.CLOSED | ViewRequirement.REPLACEABLE)), options);
            }
        } else {
            if (view.getSpecification() != null && view.getSpecification().isReplaceable() && !(view.getContent() instanceof RootObject)) {
                // offer other/alternative views
                replaceOptions(Toolkit.getViewFactory().availableViews(new ViewRequirement(content, ViewRequirement.OPEN | ViewRequirement.REPLACEABLE)), options); //content, openRootViews(content, this), options);
            }
            // TODO ask the viewer for the print option - provided by the underlying system
            // options.add(new PrintOption());
            if (getParent() != null) {
                options.add(CLOSE_OPTION);
                options.add(CLOSE_ALL_OPTION);
                options.add(CLOSE_VIEWS_FOR_OBJECT);
            }
        }

        options.add(new AbstractUserAction("Refresh view", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                refresh();
            }
        });

        options.add(new AbstractUserAction("Invalidate content", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                invalidateContent();
            }
        });

        options.add(new AbstractUserAction("Invalidate layout", UserAction.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                invalidateLayout();
            }
        });

        final UndoStack undoStack = getViewManager().getUndoStack();
        if (!undoStack.isEmpty()) {
            options.add(new AbstractUserAction("Undo " + undoStack.getNameOfUndo()) {

                @Override
                public Consent disabled(final View component) {
                    return new ConsentAbstract("", undoStack.descriptionOfUndo()) {
                        private static final long serialVersionUID = 1L;
                    };
                }

                @Override
                public void execute(final Workspace workspace, final View view, final Location at) {
                    undoStack.undoLastCommand();
                }
            });
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
