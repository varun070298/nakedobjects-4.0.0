package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a property is usable or has been disabled.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the property is disabled;
 * otherwise property is enabled.
 */
public class PropertyUsabilityEvent extends UsabilityEvent {

    private static final long serialVersionUID = 1L;

    public PropertyUsabilityEvent(final Object source, final Identifier propertyIdentifier) {
        super(source, propertyIdentifier);
    }

}
