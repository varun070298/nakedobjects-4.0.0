package org.nakedobjects.plugins.headless.viewer;

import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.applib.listeners.InteractionListener;
import org.nakedobjects.plugins.headless.viewer.internal.HeadlessViewerImpl;


/**
 * A combined {@link DomainObjectContainer} and {@link HeadlessViewer}. 
 */
public class DomainObjectContainerHeadlessViewer extends DomainObjectContainerDefault implements HeadlessViewer {

    private HeadlessViewer headlessViewerDelegate;


    // /////////////////////////////////////////////////////////////
    // Views
    // /////////////////////////////////////////////////////////////

    public <T> T view(final T domainObject) {
    	return headlessViewerDelegate.view(domainObject);
    }

    public <T> T view(final T domainObject, ExecutionMode mode) {
    	return headlessViewerDelegate.view(domainObject, mode);
    }

    public boolean isView(final Object possibleView) {
    	return headlessViewerDelegate.isView(possibleView);
    }

    // /////////////////////////////////////////////////////////////
    // Listeners
    // /////////////////////////////////////////////////////////////

    public List<InteractionListener> getListeners() {
        return headlessViewerDelegate.getListeners();
    }

    public boolean addInteractionListener(final InteractionListener listener) {
        return headlessViewerDelegate.addInteractionListener(listener);
    }

    public boolean removeInteractionListener(final InteractionListener listener) {
        return headlessViewerDelegate.removeInteractionListener(listener);
    }

    public void notifyListeners(final InteractionEvent interactionEvent) {
    	headlessViewerDelegate.notifyListeners(interactionEvent);
    }

    
    
    // /////////////////////////////////////////////////////////////
    // Dependencies (due to *Aware)
    // /////////////////////////////////////////////////////////////

    /**
     * As per superclass, but also initializes the delegate {@link HeadlessViewer}.
     */
    @Override
    public void setRuntimeContext(RuntimeContext runtimeContext) {
    	super.setRuntimeContext(runtimeContext);
    	headlessViewerDelegate = new HeadlessViewerImpl(runtimeContext);
    }


}
