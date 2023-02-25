package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether an action is visible or has been hidden.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the action is invisible;
 * otherwise action is visible.
 */
public class ActionVisibilityEvent extends VisibilityEvent {

    private static final long serialVersionUID = 1L;

    public ActionVisibilityEvent(final Object source, final Identifier actionIdentifier) {
        super(source, actionIdentifier);
    }

}
