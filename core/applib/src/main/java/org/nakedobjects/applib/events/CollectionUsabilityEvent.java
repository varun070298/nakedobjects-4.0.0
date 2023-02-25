package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a collection is usable or has been disabled.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the collection is disabled;
 * otherwise collection is enabled.
 */
public class CollectionUsabilityEvent extends UsabilityEvent {

    private static final long serialVersionUID = 1L;

    public CollectionUsabilityEvent(final Object source, final Identifier identifier) {
        super(source, identifier);
    }

}
