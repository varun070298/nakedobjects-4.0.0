package org.nakedobjects.plugins.headless.applib;

import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.applib.events.UsabilityEvent;


/**
 * Superclass of exceptions which indicate an attempt to interact with a class member that is disabled.
 */
public class DisabledException extends InteractionException {

    private static final long serialVersionUID = 1L;

    public DisabledException(final InteractionEvent interactionEvent) {
        super(interactionEvent);
    }

    @Override
    public UsabilityEvent getInteractionEvent() {
        return (UsabilityEvent) super.getInteractionEvent();
    }

}
