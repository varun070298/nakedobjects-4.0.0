package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents an access (reading) of a property.
 * 
 * <p>
 * Analogous to {@link PropertyModifyEvent}, however the {@link #getReason()} will always be <tt>null</tt>.
 * (If access is not allowed then a {@link PropertyVisibilityEvent} would have been fired).
 */
public class PropertyAccessEvent extends AccessEvent {

    private static final long serialVersionUID = 1L;

    public PropertyAccessEvent(final Object source, final Identifier propertyIdentifier, final Object value) {
        super(source, propertyIdentifier);
        this.value = value;
    }

    private final Object value;

    public Object getValue() {
        return value;
    }

}
