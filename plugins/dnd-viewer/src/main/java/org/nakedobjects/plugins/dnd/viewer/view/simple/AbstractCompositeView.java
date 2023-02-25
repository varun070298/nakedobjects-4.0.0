package org.nakedobjects.plugins.dnd.viewer.view.simple;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.AwtColor;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public abstract class AbstractCompositeView extends ObjectView {
    private static final Logger LOG = Logger.getLogger(AbstractCompositeView.class);
    private int buildCount = 0;
    private boolean buildInvalid = true;
    private boolean canDragView = true;
    private int layoutCount = 0;
    private boolean layoutInvalid = true;
    protected Vector<View> views; // TODO make private
    private FocusManager focusManager;// = new SubviewFocusManager(this);

    public AbstractCompositeView(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        views = new Vector();
    }

    @Override
    public void refresh() {
        final View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            views[i].refresh();
        }
    }

    @Override
    public void addView(final View view) {
        add(views, view);
    }

    // TODO make private
    protected void add(final Vector views, final View view) {
        LOG.debug("adding " + view + " to " + this);
        views.addElement(view);
        getViewManager().addToNotificationList(view);
        view.setParent(getView());
        invalidateLayout();
    }

    public boolean canDragView() {
        return canDragView;
    }

    @Override
    public void debug(final DebugString debug) {
        super.debug(debug);
        debug.appendln();
    }

    @Override
    public void debugStructure(final DebugString b) {
        b.appendln("Built", (buildInvalid ? "invalid, " : "") + buildCount + " builds");
        b.appendln("Laid out", (layoutInvalid ? "invalid, " : "") + layoutCount + " layouts");
        super.debugStructure(b);
    }

    @Override
    public void dispose() {
        final View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            views[i].dispose();
        }
        super.dispose();
    }

    @Override
    public void draw(final Canvas canvas) {
        final View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            final View subview = views[i];
            final Bounds bounds = subview.getBounds();
            if (Toolkit.debug) {
                LOG.debug("compare: " + bounds + "  " + canvas);
            }
            if (canvas.overlaps(bounds)) {
                // Canvas subCanvas = canvas.createSubcanvas();
                final Canvas subCanvas = canvas.createSubcanvas(bounds.getX(), bounds.getY(), bounds.getWidth() - 0, bounds
                        .getSize().getHeight());
                // subCanvas.offset(subview.getBounds().getX(), subview.getBounds().getY());
                if (Toolkit.debug) {
                    LOG.debug("-- repainting " + subview);
                    LOG.debug("subcanvas " + subCanvas);
                }
                subview.draw(subCanvas);
                if (Toolkit.debug) {
                    canvas.drawRectangle(subview.getBounds().getX(), subview.getBounds().getY(),
                    subview.getBounds().getWidth() - 1, subview.getBounds().getHeight() - 1,
                    AwtColor.DEBUG_BORDER_BOUNDS);
                }
            }
        }
    }

    @Override
    public int getBaseline() {
        final View[] e = getSubviews();
        if (e.length == 0) {
            return 14;
        } else {
            final View subview = e[0];
            return subview.getBaseline();
        }
    }

    @Override
    public FocusManager getFocusManager() {
        return focusManager == null ? super.getFocusManager() : focusManager;
    }

    @Override
    public abstract Size getMaximumSize();

    @Override
    public View[] getSubviews() {
        if (buildInvalid) {
            getFeedbackManager().setBusy(this, null);
            buildCount++;
            buildInvalid = false;
            buildView();
            getFeedbackManager().clearBusy(this);
        } 
        return subviews();
    }

    protected abstract void buildView();

    protected View[] subviews() {
        final View v[] = new View[views.size()];
        views.copyInto(v);
        return v;
    }

    @Override
    public void invalidateContent() {
        buildInvalid = true;
        invalidateLayout();
    }

    @Override
    public void invalidateLayout() {
        layoutInvalid = true;
        super.invalidateLayout();
    }

    @Override
    public void layout(final Size maximumSize) {
        if (layoutInvalid) {
            getFeedbackManager().setBusy(this, null);
            layoutCount++;
            layoutInvalid = false;
            markDamaged();
            doLayout(maximumSize);
            markDamaged();
            getFeedbackManager().clearBusy(this);
        }
    }

    protected abstract void doLayout(Size maximumSize);

    protected boolean isLayoutInvalid() {
        return layoutInvalid;
    }

    @Override
    public View subviewFor(final Location location) {
        final Location l = new Location(location);
        final Padding padding = getPadding();
        l.subtract(padding.getLeft(), padding.getTop());
        final View views[] = getSubviews();
        for (int i = views.length - 1; i >= 0; i--) {
            if (views[i].getBounds().contains(l)) {
                return views[i];
            }
        }
        return null;
    }

    @Override
    public View pickupView(final Location location) {
        return canDragView ? super.pickupView(location) : null;
    }

    @Override
    public void removeView(final View view) {
        LOG.debug("removing " + view + " from " + this);
        if (views.contains(view)) {
            views.removeElement(view);
            getViewManager().removeFromNotificationList(view);
            markDamaged();
            invalidateLayout();
        } else {
            throw new NakedObjectException(view + "\n    not in " + getView());
        }
    }

    @Override
    public void replaceView(final View toReplace, final View replacement) {
        for (int i = 0; i < views.size(); i++) {
            if (views.elementAt(i) == toReplace) {
                replacement.setParent(getView());
                replacement.setLocation(toReplace.getLocation());
                views.insertElementAt(replacement, i);
                invalidateLayout();
                toReplace.dispose();
                getViewManager().addToNotificationList(replacement);
                return;
            }
        }

        throw new NakedObjectException(toReplace + " not found to replace");
    }

    public void setCanDragView(final boolean canDragView) {
        this.canDragView = canDragView;
    }

    @Override
    public void setFocusManager(final FocusManager focusManager) {
        this.focusManager = focusManager;
    }

    @Override
    public String toString() {
        final ToString to = new ToString(this, getId());
        to.append("type", getSpecification().getName());
        return to.toString();
    }

    @Override
    public void update(final NakedObject object) {
        LOG.debug("update notify on " + this);
        invalidateContent();
    }

    @Override
    public ViewAreaType viewAreaType(final Location location) {
        final View subview = subviewFor(location);
        if (subview == null) {
            return ViewAreaType.VIEW;
        } else {
            location.subtract(subview.getLocation());
            return subview.viewAreaType(location);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
