package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;


public abstract class ObjectView extends AbstractView {

    public ObjectView(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }

    @Override
    public void dragIn(final ContentDrag drag) {
        final Consent consent = getContent().canDrop(drag.getSourceContent());
        final String description = getContent().getDescription();
        if (consent.isAllowed()) {
            getFeedbackManager().setAction(consent.getDescription() + " " + description);
            getState().setCanDrop();
        } else {
            getFeedbackManager().setAction(consent.getReason() + " " + description);
            getState().setCantDrop();
        }
        markDamaged();
    }

    @Override
    public void dragOut(final ContentDrag drag) {
        getState().clearObjectIdentified();
        markDamaged();
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        final View subview = subviewFor(drag.getLocation());
        if (subview != null) {
            drag.subtract(subview.getLocation());
            return subview.dragStart(drag);
        } else {
            if (drag.isCtrl()) {
                final View dragOverlay = new DragViewOutline(getView());
                return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
            } else {
                final View dragOverlay = Toolkit.getViewFactory().getContentDragSpecification().createView(getContent(), null);
                return new ContentDrag(this, drag.getLocation(), dragOverlay);
            }
        }
    }

    /**
     * Called when a dragged object is dropped onto this view. The default behaviour implemented here calls
     * the action method on the target, passing the source object in as the only parameter.
     */
    @Override
    public void drop(final ContentDrag drag) {
        final NakedObject result = getContent().drop(drag.getSourceContent());
        if (result != null) {
            final Location location = new Location();
            location.move(10, 10);
            getWorkspace().objectActionResult(result, location);
        }
        getState().clearObjectIdentified();
        getFeedbackManager().showMessagesAndWarnings();

        markDamaged();
    }

    @Override
    public void firstClick(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.firstClick(click);
        } else {
            if (click.button2()) {
                final Location location = new Location(click.getLocationWithinViewer());
                getViewManager().showInOverlay(getContent(), location);
            }
        }
    }

    @Override
    public void invalidateContent() {
        super.invalidateLayout();
    }

    @Override
    public void secondClick(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.secondClick(click);
        } else {
            final Location location = getAbsoluteLocation();
            location.translate(click.getLocation());
            getWorkspace().addWindowFor(getContent().getNaked(), location);
        }
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        super.contentMenuOptions(options);

        options.add(new AbstractUserAction("Reload", NakedObjectActionConstants.DEBUG) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final NakedObject object = getContent().getNaked();
                getPersistenceSession().reload(object);
            }

        });
    }
    
    /////////////////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////////////////////////
    
    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

}
// Copyright (c) Naked Objects Group Ltd.
