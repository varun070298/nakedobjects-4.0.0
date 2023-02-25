package org.nakedobjects.plugins.headless.applib;

import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.applib.events.ValidityEvent;


/**
 * Superclass of exceptions which indicate an attempt to interact with an object or member in a way that is
 * invalid.
 * 
 * <p>
 * 
 */
public class InvalidException extends InteractionException {

    private static final long serialVersionUID = 1L;

    public InvalidException(final InteractionEvent interactionEvent) {
        super(interactionEvent);
    }

    @Override
    public ValidityEvent getInteractionEvent() {
        return (ValidityEvent) super.getInteractionEvent();
    }

}
