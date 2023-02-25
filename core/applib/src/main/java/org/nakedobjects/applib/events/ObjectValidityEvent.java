package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether the current values of the properties/collections of an object are valid
 * (for example, prior to saving that object).
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the object is invalid,
 * otherwise ok.
 */
public class ObjectValidityEvent extends ValidityEvent implements ProposedHolderEvent {

    private static final long serialVersionUID = 1L;

    public ObjectValidityEvent(final Object source, final Identifier classIdentifier) {
        super(source, classIdentifier);
    }

	public Object getProposed() {
		return getSource();
	}

}
