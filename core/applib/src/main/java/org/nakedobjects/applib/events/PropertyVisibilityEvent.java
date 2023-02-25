package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a property is visible or has been hidden.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the property is invisible;
 * otherwise property is visible.
 */
public class PropertyVisibilityEvent extends VisibilityEvent {

    private static final long serialVersionUID = 1L;

    public PropertyVisibilityEvent(final Object source, final Identifier propertyIdentifier) {
        super(source, propertyIdentifier);
    }

}
