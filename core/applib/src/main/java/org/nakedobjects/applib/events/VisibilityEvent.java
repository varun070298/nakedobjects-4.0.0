package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check to determine whether a member of an object is visible or has been hidden.
 * 
 * <p>
 * If {@link #getReason()} is <tt>null</tt>, then is usable; otherwise is invisible.
 * 
 * @see AccessEvent
 * @see UsabilityEvent
 * @see ValidityEvent
 */
public abstract class VisibilityEvent extends InteractionEvent {

    private static final long serialVersionUID = 1L;

    public VisibilityEvent(final Object source, final Identifier identifier) {
        super(source, identifier);
    }

}
