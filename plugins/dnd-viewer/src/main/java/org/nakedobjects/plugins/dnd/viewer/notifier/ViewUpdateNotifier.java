package org.nakedobjects.plugins.dnd.viewer.notifier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.content.RootCollection;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


public class ViewUpdateNotifier implements DebugInfo {
    private static final Logger LOG = Logger.getLogger(ViewUpdateNotifier.class);
    protected Hashtable<NakedObject,Vector<View>> viewListByAdapter = new Hashtable<NakedObject,Vector<View>>();

    public void add(final View view) {
        final Content content = view.getContent();
        if (content != null && content.isObject()) {
            final NakedObject adapter = content.getNaked();

            if (adapter != null) {
                Vector<View> viewsToNotify;

                if (viewListByAdapter.containsKey(adapter)) {
                    viewsToNotify = viewListByAdapter.get(adapter);
                } else {
                    viewsToNotify = new Vector<View>();
                    viewListByAdapter.put(adapter, viewsToNotify);
                }

                if (viewsToNotify.contains(view)) {
                    throw new NakedObjectException(view + " already being notified");
                }
                viewsToNotify.addElement(view);
                if (LOG.isDebugEnabled()) {
                	LOG.debug("added " + view + " to observers for " + adapter);
                }
            }
        }
    }

    public void debugData(final DebugString buf) {
        final Enumeration<NakedObject> f = viewListByAdapter.keys();

        while (f.hasMoreElements()) {
            final Object object = f.nextElement();

            final Vector<View> viewsToNotify = viewListByAdapter.get(object);
            final Enumeration<View> e = viewsToNotify.elements();
            buf.append("Views for " + object + " \n");

            while (e.hasMoreElements()) {
                final View view = e.nextElement();
                buf.append("        " + view);
                buf.append("\n");
            }
            buf.append("\n");
        }
    }

    public String debugTitle() {
        return "Views for object details (observers)";
    }

    public void remove(final View view) {
        final Content content = view.getContent();
        if (content == null || !content.isObject()) {
        	// nothing to do
        	return;
        }
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("removing " + content + " for " + view);
    	}

    	final NakedObject object = ((ObjectContent) content).getObject();
        if (object != null) {
            if (!viewListByAdapter.containsKey(object)) {
                throw new NakedObjectException("Tried to remove a non-existant view " + view + " from observers for "
                        + object);
            }
            Vector<View> viewsToNotify;
            viewsToNotify = viewListByAdapter.get(object);
            final Enumeration<View> e = viewsToNotify.elements();
            while (e.hasMoreElements()) {
                final View v = (View) e.nextElement();
                if (view == v.getView()) {
                    viewsToNotify.remove(v);
                    LOG.debug("removed " + view + " from observers for " + object);
                    break;
                }
            }

            if (viewsToNotify.size() == 0) {
                viewListByAdapter.remove(object);
                if (LOG.isDebugEnabled()) {
                	LOG.debug("removed observer list for " + object);
                }

                // TODO need to do garbage collection instead
                // NakedObjectLoader loader = NakedObjects.getObjectLoader();
                // loader.unloaded((NakedObject) object);
            }
        }
    }

    public void shutdown() {
        viewListByAdapter.clear();
    }

    public void invalidateViewsForChangedObjects() {
        for(NakedObject object:getUpdateNotifier().getChangedObjects()) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("invalidate views for " + object);
        	}
            final Vector<View> viewsVector = this.viewListByAdapter.get(object);
            if (viewsVector == null) {
                continue;
            }
            final Enumeration<View> views = viewsVector.elements();
            while (views.hasMoreElements()) {
                final View view = (View) views.nextElement();
                LOG.debug("   - " + view);
                view.getView().invalidateContent();
            }
        }
    }

    public void removeViewsForDisposedObjects() {
        for(NakedObject objectToDispose: getUpdateNotifier().getDisposedObjects()) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("dispose views for " + objectToDispose);
        	}
            final Vector<View> viewsForObject = viewListByAdapter.get(objectToDispose);
            if (viewsForObject == null) {
            	continue;
            }
            removeViews(viewsForObject);
            final Vector<View> remainingViews = viewListByAdapter.get(objectToDispose);
            if (remainingViews != null && remainingViews.size() > 0) {
                getMessageBroker().addWarning(
                        "There are still views (within other views) for the disposed object " + objectToDispose.titleString()
                                + ".  Only objects that are shown as root views can be properly disposed of");
            } else {
                getAdapterManager().removeAdapter(objectToDispose);
            }
        }
    }

    private void removeViews(final Vector<View> views) {
        final View[] viewsArray = new View[views.size()];
        views.copyInto(viewsArray);
        final View[] viewsOnWorkspace = viewsArray[0].getWorkspace().getSubviews();
        for (int i = 0; i < viewsArray.length; i++) {
            final View view = viewsArray[i].getView();
            for (int j = 0; j < viewsOnWorkspace.length; j++) {
                if (view == viewsOnWorkspace[j]) {
                    LOG.debug("   (root removed) " + view);
                    view.getView().dispose();
                    break;
                }
            }

            for (int j = 0; j < viewsOnWorkspace.length; j++) {
                if (viewsOnWorkspace[j].getContent() instanceof RootCollection) {
                    final View[] subviewsOfRootView = viewsOnWorkspace[j].getSubviews();
                    for (int k = 0; k < subviewsOfRootView.length; k++) {
                        if (subviewsOfRootView[k] == view) {
                            LOG.debug("   (element removed) " + view);
                            view.getView().dispose();
                        }
                    }
                }
            }

            for (int j = 0; j < viewsOnWorkspace.length; j++) {
                if (viewsOnWorkspace[j].contains(view)) {
                    LOG.debug("   (invalidated) " + view);
                    final View parent = view.getParent();
                    parent.invalidateContent();
                }
            }

        }

    }

    
    //////////////////////////////////////////////////////////////////
    // Dependencies (from singleton)
    //////////////////////////////////////////////////////////////////
    
    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static MessageBroker getMessageBroker() {
        return NakedObjectsContext.getMessageBroker();
    }

	private static UpdateNotifier getUpdateNotifier() {
		return NakedObjectsContext.inSession() ? NakedObjectsContext.getUpdateNotifier() : new NoOpUpdateNotifier();
	}

}

class NoOpUpdateNotifier implements UpdateNotifier {

    public void addChangedObject(NakedObject object) {}

    public void addDisposedObject(NakedObject adapter) {}

    public void clear() {}

    public void ensureEmpty() {}

    public List<NakedObject> getChangedObjects() {
        return new ArrayList<NakedObject>();
    }

    public List<NakedObject> getDisposedObjects() {
        return new ArrayList<NakedObject>();
    }

    public void injectInto(Object candidate) {}

}
// Copyright (c) Naked Objects Group Ltd.
