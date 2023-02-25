package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a collection is visible or has been hidden.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the collection is invisible;
 * otherwise collection is visible.
 */
public class CollectionVisibilityEvent extends VisibilityEvent {

    private static final long serialVersionUID = 1L;

    public CollectionVisibilityEvent(final Object source, final Identifier identifier) {
        super(source, identifier);
    }

}
