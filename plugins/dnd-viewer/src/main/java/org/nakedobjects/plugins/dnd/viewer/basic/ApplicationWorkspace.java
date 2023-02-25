package org.nakedobjects.plugins.dnd.viewer.basic;

import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectList;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.border.DialogBorder;
import org.nakedobjects.plugins.dnd.viewer.border.WindowBorder;
import org.nakedobjects.plugins.dnd.viewer.content.PerspectiveContent;
import org.nakedobjects.plugins.dnd.viewer.content.ServiceObject;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.focus.SubviewFocusManager;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;
import org.nakedobjects.runtime.authentication.standard.exploration.MultiUserExplorationSession;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;


public final class ApplicationWorkspace extends CompositeViewUsingBuilder implements Workspace {
    protected Vector<View> serviceViews;
    protected Vector<View> iconViews;

    public ApplicationWorkspace(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        serviceViews = new Vector<View>();
        iconViews = new Vector<View>();
    }

    public void addDialog(final View dialogContent) {
        DialogBorder dialogView = new DialogBorder(dialogContent, false);
        addView(dialogView);
      //  dialogView.setFocusManager(   new SubviewFocusManager(dialogView));
    }

    public void addWindow(View containedView) {
        boolean scrollable = !containedView.getSpecification().isResizeable();
        WindowBorder windowView = new WindowBorder(containedView, scrollable);
        addView(windowView);
        windowView.setFocusManager(   new SubviewFocusManager(windowView));
    }

    @Override
    public void addView(final View view) {
        super.addView(view);
        getViewManager().setKeyboardFocus(view);
        view.getFocusManager().focusFirstChildView();
    }

    @Override
    public void replaceView(final View toReplace, final View replacement) {
        if (replacement.getSpecification().isOpen()) {
            boolean scrollable = !replacement.getSpecification().isResizeable();
            WindowBorder windowView = new WindowBorder(replacement, scrollable);
            super.replaceView(toReplace, windowView);
        } else {
            super.replaceView(toReplace, replacement);
        }
    }

    public View addWindowFor(final NakedObject object, final Location at) {
        final Content content = Toolkit.getContentFactory().createRootContent(object);
        final View view = Toolkit.getViewFactory().createView(new ViewRequirement(content, ViewRequirement.OPEN));
        view.setLocation(at);
        addWindow(view);
        getViewManager().setKeyboardFocus(view);
        return view;
    }

    public View addIconFor(final NakedObject object, final Location at) {
        final Content content = Toolkit.getContentFactory().createRootContent(object);
        View icon = Toolkit.getViewFactory().createView(new ViewRequirement(content, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW));
        add(iconViews, icon);
        icon.setLocation(at);
        return icon;
    }
    
    public void addServiceIconFor(final NakedObject service) {
         final Content content = new ServiceObject(service);
        final View serviceIcon = Toolkit.getViewFactory().createView(new ViewRequirement(content, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW));
        add(serviceViews, serviceIcon);
    }
    
    @Override
    public Drag dragStart(final DragStart drag) {
        final View subview = subviewFor(drag.getLocation());
        if (subview != null) {
            drag.subtract(subview.getLocation());
            return subview.dragStart(drag);
        } else {
            return null;
        }
    }


    // TODO check the dragging in of objects, flag to user that object cannot be dropped
    @Override
    public void drop(final ContentDrag drag) {
        getFeedbackManager().showDefaultCursor();

        if (!drag.getSourceContent().isObject()) {
            return;
        }

        if (drag.getSourceContent().getNaked() == getPerspective()) {
            getFeedbackManager().setAction("can' drop self on workspace");
            return;
        }

        final NakedObject source = ((ObjectContent) drag.getSourceContent()).getObject();
        if (source.getSpecification().isService()) {
            getPerspective().addToServices(source.getObject());
            invalidateContent();
        } else {
            if (!drag.isShift()) {
                getPerspective().addToObjects(source.getObject());
            }
        }

        View newView;
        if (source.getSpecification().isService()) {
            return;
        } else {
            final Location dropLocation = drag.getTargetLocation();
            dropLocation.subtract(drag.getOffset());

            if (drag.isShift()) {
                newView = createSubviewFor(source, false);
                drag.getTargetView().addView(newView);
                newView.setLocation(dropLocation);
            } else {
                // place object onto desktop as icon
                final View sourceView = drag.getSource();
                if (!sourceView.getSpecification().isOpen()) {
                    final View[] subviews = getSubviews();
                    for (int i = 0; i < subviews.length; i++) {
                        if (subviews[i] == sourceView) {
                            sourceView.markDamaged();
                            sourceView.setLocation(dropLocation);
                            sourceView.markDamaged();
                            return;
                        }
                    }
                } else {
                    for (View view : iconViews) {
                        if (view.getContent().getNaked() == source) {
                            view.markDamaged();
                            view.setLocation(dropLocation);
                            view.markDamaged();
                            return;
                        }
                    }
                }
                addIconFor(source, dropLocation);
            }
        }
    }

    @Override
    public void entered() {
    // prevents status details about "Persective..."
    }

    private PerspectiveEntry getPerspective() {
        return ((PerspectiveContent) getContent()).getPerspective();
    }

    public View createSubviewFor(final NakedObject object, final boolean asIcon) {
        View view;
        final Content content = Toolkit.getContentFactory().createRootContent(object);
        if (asIcon) {
            view = Toolkit.getViewFactory().createView(new ViewRequirement(content, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW));
        } else {
            view = Toolkit.getViewFactory().createView(new ViewRequirement(content, ViewRequirement.OPEN | ViewRequirement.SUBVIEW));
        }
        return view;
    }

    @Override
    public void drop(final ViewDrag drag) {
        getFeedbackManager().showDefaultCursor();

        final View sourceView = drag.getSourceView();
        if (sourceView.getSpecification() != null && sourceView.getSpecification().isSubView()) {
            if (sourceView.getSpecification().isOpen() && sourceView.getSpecification().isReplaceable()) {
                // TODO remove the open view from the container and place on
                // workspace; replace the internal view with an icon
            } else {
                final Location newLocation = drag.getViewDropLocation();
                addWindowFor(sourceView.getContent().getNaked(), newLocation);
                sourceView.getState().clearViewIdentified();
            }
        } else {
            sourceView.markDamaged();
            final Location newLocation = drag.getViewDropLocation();
            sourceView.setLocation(newLocation);
            sourceView.limitBoundsWithin(getSize());
            sourceView.markDamaged();
        }
    }

    @Override
    public Padding getPadding() {
        return new Padding();
    }

    @Override
    public Workspace getWorkspace() {
        return this;
    }

    public void lower(final View view) {
        if (views.contains(view)) {
            views.removeElement(view);
            views.insertElementAt(view, 0);
            markDamaged();
        }
    }

    public void raise(final View view) {
        if (views.contains(view)) {
            views.removeElement(view);
            views.addElement(view);
            markDamaged();
        }
    }

    @Override
    public void removeView(final View view) {
        view.markDamaged();
        if (iconViews.contains(view)) {
            iconViews.remove(view);
            getViewManager().removeFromNotificationList(view);
            removeObject(view.getContent().getNaked());
        } else if (serviceViews.contains(view)) {
            serviceViews.remove(view);
            getViewManager().removeFromNotificationList(view);
            removeService(view.getContent().getNaked());
        } else {
            super.removeView(view);
        }
    }

    private void removeService(NakedObject object) {
        getPerspective().removeFromServices(object.getObject());
    }

    private void removeObject(final NakedObject object) {
        getPerspective().removeFromObjects(object.getObject());
    }

    public void removeViewsFor(final NakedObject object) {
        final View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            final View view = views[i];
            if (view.getContent().getNaked() == object) {
                view.dispose();
            }
        }
    }

    @Override
    public void secondClick(final Click click) {
        final View subview = subviewFor(click.getLocation());
        if (subview != null) {
            // ignore double-click on self - don't open up new view
            super.secondClick(click);
        }
    }

    @Override
    public String toString() {
        return "Workspace" + getId();
    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_WORKSPACE));

        options.add(new AbstractUserAction("Close all") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final View views[] = getWindowViews();
                for (int i = 0; i < views.length; i++) {
                    final View v = views[i];
                    // if (v.getSpecification().isOpen()) {
                    v.dispose();
                    // }
                }
                markDamaged();
            }
        });

        options.add(new AbstractUserAction("Tidy up windows") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                tidyViews(getWindowViews());
            }
        });

        options.add(new AbstractUserAction("Tidy up icons") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                tidyViews(getObjectIconViews());
            }
        });

        options.add(new AbstractUserAction("Tidy up all") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                tidyViews(getObjectIconViews());
                tidyViews(getWindowViews());
            }
        });

        options.add(new AbstractUserAction("Services...") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final List<Object> services = NakedObjectsContext.getServices();
                NakedObject[] serviceObjects = new NakedObject[services.size()];
                int i = 0;
                for (Object object : services) {
                    AdapterManager adapterManager = NakedObjectsContext.getPersistenceSession().getAdapterManager();
                    serviceObjects[i++] = adapterManager.adapterFor(object);
                }
                final NakedObjectSpecification spec = getSpecificationLoader().loadSpecification(Object.class);
                final NakedObjectList collection = new NakedObjectList(spec, serviceObjects);
                addWindowFor(getAdapterManager().adapterFor(collection), at);
            }
        });

        menuForChangingUsers(options);
    }

    private void menuForChangingUsers(final UserActionSet options) {
        // TODO pick out users from the perspectives, but only show when in exploration mode
        if (getAuthenticationSession() instanceof MultiUserExplorationSession) {
            MultiUserExplorationSession session = (MultiUserExplorationSession) getAuthenticationSession();
            
            Set<String> users = session.getUserNames();
            final UserActionSet set = new UserActionSet("Change user", options, NakedObjectActionConstants.EXPLORATION);
            for (String user : users) {
                menuOptionForChangingUser(set, user, session.getUserName());
            }
            options.add(set);
        }
    }

    private void menuOptionForChangingUser(final UserActionSet set, final String user, final String currentUser) {
        set.add(new AbstractUserAction(user) {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final MultiUserExplorationSession session = (MultiUserExplorationSession) getAuthenticationSession();
                session.setCurrentSession(user);
            }
            
            @Override
            public Consent disabled(View view) {
                return user.equals(currentUser) ? new Veto("Current user") : Allow.DEFAULT;
            }
        });
    }

    @Override
    protected View[] subviews() {
        final View v[] = new View[views.size() + serviceViews.size() + iconViews.size()];
        int offset = 0;
        Object[] src = serviceViews.toArray();
        System.arraycopy(src, 0, v, offset, src.length);
        offset += src.length;
        src = iconViews.toArray();
        System.arraycopy(src, 0, v, offset, src.length);
        offset += src.length;
        src = views.toArray();
        System.arraycopy(src, 0, v, offset, src.length);
        return v;
    }

    public void clearServiceViews() {
        final Enumeration e = serviceViews.elements();
        while (e.hasMoreElements()) {
            final View view = (View) e.nextElement();
            view.markDamaged();
        }
        serviceViews.clear();
    }

    protected View[] getWindowViews() {
        return createArrayOfViews(views);
    }

    private View[] createArrayOfViews(final Vector views) {
        final View[] array = new View[views.size()];
        views.copyInto(array);
        return array;
    }

    protected View[] getServiceIconViews() {
        return createArrayOfViews(serviceViews);
    }

    protected View[] getObjectIconViews() {
        return createArrayOfViews(iconViews);
    }

    private void tidyViews(final View[] views) {
        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            v.setLocation(ApplicationWorkspaceBuilder.UNPLACED);
        }
        invalidateLayout();
        markDamaged();
    }

    // //////////////////////////////////////////////////////////////////
    // Dependencies (from singleton)
    // //////////////////////////////////////////////////////////////////

    private SpecificationLoader getSpecificationLoader() {
        return NakedObjectsContext.getSpecificationLoader();
    }

    private PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private AuthenticationSession getAuthenticationSession() {
        return NakedObjectsContext.getAuthenticationSession();
    }

}
// Copyright (c) Naked Objects Group Ltd.
