package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether the proposed values of the value type is valid.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the proposed value is invalid,
 * otherwise the new value is acceptable.
 */
public class ParseValueEvent extends ValidityEvent implements ProposedHolderEvent {

    private static final long serialVersionUID = 1L;

	private static Object coalesce(final Object source, final String proposed) {
		return source != null?source:proposed;
	}

	private final String proposed;

    public ParseValueEvent(final Object source, final Identifier classIdentifier, final String proposed) {
        super(coalesce(source, proposed), classIdentifier);
        this.proposed = proposed;
    }


    /**
     * Will be the source provided in the {@link #ParseValueEvent(Object, Identifier, String) constructor}
     * if not null, otherwise will fallback to the proposed value.
     */
    @Override
    public Object getSource() {
    	return super.getSource();
    }

	public String getProposed() {
		return proposed;
	}

}
