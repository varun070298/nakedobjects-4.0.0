package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether a particular value for a property is valid or not.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the value is invalid; otherwise
 * the value is valid.
 */
public class PropertyModifyEvent extends ValidityEvent implements ProposedHolderEvent {

    private static final long serialVersionUID = 1L;

    private final Object proposed;

    public PropertyModifyEvent(final Object source, final Identifier propertyIdentifier, final Object proposed) {
        super(source, propertyIdentifier);
        this.proposed = proposed;
    }

    /**
     * If <tt>null</tt>, then the property was cleared.
     * 
     * @return
     */
    public Object getProposed() {
        return proposed;
    }

}
