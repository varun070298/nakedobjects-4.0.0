package org.nakedobjects.applib.events;

import org.nakedobjects.applib.Identifier;


/**
 * Represents a check as to whether an action is usable or has been disabled.
 * 
 * <p>
 * If {@link #getReason()} is not <tt>null</tt> then provides the reason why the action is disabled;
 * otherwise action is enabled.
 */
public class ActionUsabilityEvent extends UsabilityEvent {

    private static final long serialVersionUID = 1L;

    public ActionUsabilityEvent(final Object source, final Identifier actionIdentifier) {
        super(source, actionIdentifier);
    }

}
