package org.nakedobjects.plugins.headless.viewer.internal;

import org.nakedobjects.applib.events.InteractionEvent;


public abstract class InteractionEventDispatcherTypeSafe<T extends InteractionEvent> implements InteractionEventDispatcher {

    public abstract void dispatchTypeSafe(T interactionEvent);

    @SuppressWarnings("unchecked")
    public void dispatch(final InteractionEvent interactionEvent) {
        dispatchTypeSafe((T) interactionEvent);
    }

}
