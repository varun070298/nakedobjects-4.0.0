package org.nakedobjects.plugins.headless.viewer.internal;

import org.nakedobjects.applib.events.InteractionEvent;


public interface InteractionEventDispatcher {

    void dispatch(InteractionEvent interactionEvent);

}
