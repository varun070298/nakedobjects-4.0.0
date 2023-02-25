package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


/**
 * Destroy this object
 */
public class DisposeObjectOption extends AbstractUserAction {
    public DisposeObjectOption() {
        super("Dispose Object", EXPLORATION);
    }

    @Override
    public Consent disabled(final View view) {
        final NakedObject adapter = view.getContent().getNaked();
        if (adapter.getResolveState().isDestroyed()) {
            // TODO: move logic into Facet
            return new Veto("Can't do anything with a destroyed object");
        }
        if (isObjectInRootView(view)) {
            return Allow.DEFAULT;
        } else {
            // TODO: move logic into Facet
            return new Veto("Can't dispose an object from within another view.");
        }
    }

    private boolean isObjectInRootView(final View view) {
        final View rootView = rootView(view);
        return view.getContent() == rootView.getContent();
    }

    private View rootView(final View view) {
        final View parent = view.getParent();
        if (view.getWorkspace() == parent) {
            return view;
        } else {
            return rootView(parent);
        }
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {

        final NakedObject object = ((ObjectContent) view.getContent()).getObject();

        // xactn mgmt now done by PersistenceSession#destroyObject()
        // getTransactionManager().startTransaction();

        getPersistenceSession().destroyObject(object);
        
        // getTransactionManager().endTransaction();

        getUpdateNotifier().addDisposedObject(object);
        view.getViewManager().disposeUnneededViews();
        view.getFeedbackManager().showMessagesAndWarnings();
    }


    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }
    
    private static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }
    
    private static UpdateNotifier getUpdateNotifier() {
        return NakedObjectsContext.getUpdateNotifier();
    }

}
// Copyright (c) Naked Objects Group Ltd.
