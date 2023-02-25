package org.nakedobjects.plugins.headless.applib;

import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.applib.events.VisibilityEvent;


/**
 * Superclass of exceptions which indicate an attempt to interact with a class member that is in some way
 * hidden or invisible.
 */
public class HiddenException extends InteractionException {

    private static final long serialVersionUID = 1L;

    public HiddenException(final InteractionEvent interactionEvent) {
        super(interactionEvent);
    }

    @Override
    public VisibilityEvent getInteractionEvent() {
        return (VisibilityEvent) super.getInteractionEvent();
    }

}
