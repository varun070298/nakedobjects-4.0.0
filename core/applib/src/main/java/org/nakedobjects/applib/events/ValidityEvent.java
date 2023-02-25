package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check to determine whether a proposed change is valid.
 * 
 * <p>
 * Multiple subclasses, including:
 * <ul>
 * <li>modifying a property</li>
 * <li>adding to/removing from a collection</li>
 * <li>checking a single argument for an action invocation</li>
 * <li>checking all arguments for an action invocation</li>
 * <li>checking all properties for an object before saving</li>
 * </ul>
 * 
 * <p>
 * If {@link #getReason()} is <tt>null</tt>, then is usable; otherwise is disabled.
 * 
 * @see AccessEvent
 * @see VisibilityEvent
 * @see UsabilityEvent
 */
public abstract class ValidityEvent extends InteractionEvent {

    private static final long serialVersionUID = 1L;

    public ValidityEvent(final Object source, final Identifier identifier) {
        super(source, identifier);
    }

    @Override
    public Object getSource() {
    	return super.getSource();
    }
}
