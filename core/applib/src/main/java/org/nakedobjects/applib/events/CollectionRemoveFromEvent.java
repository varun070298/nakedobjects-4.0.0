package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a particular object to be removed from a collection is valid or not.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the object is invalid;
 * otherwise the object is valid.
 * 
 * @see CollectionAddToEvent
 */
public class CollectionRemoveFromEvent extends ValidityEvent implements ProposedHolderEvent {

    private static final long serialVersionUID = 1L;

    private final Object proposed;

    public CollectionRemoveFromEvent(final Object source, final Identifier collectionIdentifier, final Object proposed) {
        super(source, collectionIdentifier);
        this.proposed = proposed;
    }

    public Object getProposed() {
        return proposed;
    }

}
