package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents an access (reading) of a property, collection or title.
 * 
 * <p>
 * Analogous to {@link ValidityEvent} (which corresponds to modifying a property or collection etc), however
 * the {@link #getReason()} will always be <tt>null</tt>. (If access is not allowed then a vetoing
 * {@link VisibilityEvent} would have been fired).
 * 
 * @see UsabilityEvent
 * @see VisibilityEvent
 * @see ValidityEvent
 */
public abstract class AccessEvent extends InteractionEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AccessEvent(final Object source, final Identifier identifier) {
        super(source, identifier);
    }

}
