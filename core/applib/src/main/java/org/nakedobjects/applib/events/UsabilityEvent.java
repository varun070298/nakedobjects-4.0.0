package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check to determine whether a member of an object is usable or has been disabled.
 * 
 * <p>
 * If {@link #getReason()} is <tt>null</tt>, then is usable; otherwise is disabled.
 * 
 * @see AccessEvent
 * @see VisibilityEvent
 * @see ValidityEvent
 */
public abstract class UsabilityEvent extends InteractionEvent {

    private static final long serialVersionUID = 1L;

    public UsabilityEvent(final Object source, final Identifier identifier) {
        super(source, identifier);
    }

}
