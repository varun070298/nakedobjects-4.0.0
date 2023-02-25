package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents an access (reading) of a collection.
 * 
 * <p>
 * Analogous to {@link CollectionAddToEvent} or {@link CollectionRemoveFromEvent}, however the
 * {@link #getReason()} will always be <tt>null</tt>. (If access is not allowed then a vetoing
 * {@link CollectionVisibilityEvent} would have been fired).
 */
public class CollectionAccessEvent extends AccessEvent {

    private static final long serialVersionUID = 1L;

    public CollectionAccessEvent(final Object source, final Identifier collectionIdentifier) {
        super(source, collectionIdentifier);
    }

}
